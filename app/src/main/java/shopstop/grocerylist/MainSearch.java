package shopstop.grocerylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.*;
import com.parse.Parse;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainSearch extends ActionBarActivity {

    private EditText mFindItem;
    private EditText mLocation;
    private EditText mDistance;
    private Button  mSearchButton;
    private String api_key = "b000281a6ef7ab7de23ce178afd6faf3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();

        mFindItem = (EditText)findViewById(R.id.findItemName);
        mLocation = (EditText)findViewById(R.id.location);
        mDistance = (EditText)findViewById(R.id.disance);
        mSearchButton = (Button) findViewById(R.id.searchButton);

        setTitle("Search");

        Bundle bundle = getIntent().getExtras();
        String fillItem = null;

        mFindItem.setText("");
        mLocation.setText("");
        mDistance.setText("");

        if(bundle != null) {
            fillItem = bundle.getString("itemName");
            if(fillItem != null)
                mFindItem.setText(fillItem);
        } else {
            Log.d("Bundle Check", "Activity was passed nothing");
        }


        setListeners();


        final Activity act = this;

        // Initialize Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "F9eyFqMsPkNv18Xo9w4h9K7wya49YIbiTFcV1fny", "ZV4SwkGbpNbT9EpIqgw7WfMQSb7w7ocIZicSSb4y");

        final Button scanButton = (Button) findViewById(R.id.barcode);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(act);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setResultDisplayDuration(0);
                integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
            }
        });

        Button addButton = (Button) findViewById(R.id.addPrice);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AddItem.class);
                startActivity(intent);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            HttpClient client = new DefaultHttpClient();
            String url = "http://api.upcdatabase.org/json/" + api_key + "/0" + scanResult.getContents();
            Log.d("Trying to get this", url);
            new GetHTTPBarcode().execute(url);
        }
        else {
            Toast.makeText(getApplicationContext(), "no result", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
            return rootView;
        }
    }

    public void setListeners() {

        //Item Validation
        mFindItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mFindItem.getText().toString();

                if(mFindItem.getText().toString().equals("")) {
                    mFindItem.setError(mFindItem.getHint() + " is required!");
                    mFindItem.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("^[a-zA-Z0-9][a-zA-Z0-9 .,\\-\\/\\(\\)]++")) {
                    mFindItem.setError("Only Alphanumeric and special characters (,),., and - are allowed!");
                    mFindItem.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mFindItem.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mFindItem.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Distance Validation
        mDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mDistance.getText().toString();

                if(mDistance.getText().toString().equals("")) {
                    mDistance.setError(mFindItem.getHint() + " is required!");
                    mDistance.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("\\d+[.]?\\d*")) {
                    mDistance.setError("Only numbers are allowed!");
                    mDistance.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mDistance.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mDistance.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Location Validation
        mLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mLocation.getText().toString();

                if(mLocation.getText().toString().equals("")) {
                    mLocation.setError(mFindItem.getHint() + " is required!");
                    mLocation.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("[a-zA-Z0-9 ]++")) {
                    mLocation.setError("Only alphanumeric characters are allowed!");
                    mLocation.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mLocation.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mLocation.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Ensure Validated
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFindItem.getText().toString().equals("") || mFindItem.getError() != null ||
                 mDistance.getText().toString().equals("")|| mDistance.getError() != null ||
                 mLocation.getText().toString().equals("") || mLocation.getError() != null) {
                    ;
                } else {
                    Intent intent = new Intent(getApplication(), SearchResults.class);

                    // Set the name of the item
                    intent.putExtra("itemName", "Juice");
                    // Set the location & radius
                    intent.putExtra("latitude", 35.28304);
                    intent.putExtra("longitude", -120.65925);
                    intent.putExtra("radius", 10.0);

                    startActivity(intent);
                }
            }
        });
    }

    private class GetHTTPBarcode extends AsyncTask<String, String, JSONObject> {

        protected JSONObject doInBackground(String ... uri) {
            StringBuilder builder = new StringBuilder();
            JSONObject jsonObject = new JSONObject();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri[0]);
            try{
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if(statusCode == 200){
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while((line = reader.readLine()) != null){
                        builder.append(line);
                    }
                    jsonObject = new JSONObject(builder.toString());
                }
                else {
                    Log.d("Bad error code", "" + statusCode);
                }
            }catch(ClientProtocolException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        protected void onProgressUpdate() {
            Log.d("Response", "updating");
        }

        protected void onPostExecute(JSONObject result) {
            try {
                String name = result.get("itemname").toString();
                mFindItem.setText(name);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Item not found in database.", Toast.LENGTH_LONG).show();
                mFindItem.setText("");
            }
        }
    }
}
