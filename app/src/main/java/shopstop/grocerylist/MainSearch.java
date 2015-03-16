package shopstop.grocerylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import shopstop.grocerylist.tasks.GetBarcode;
import shopstop.grocerylist.tasks.HTTPResponse;

public class MainSearch extends ActionBarActivity implements HTTPResponse {

    private EditText mFindItem;
    private EditText mLocation;
    private EditText mDistance;
    private Button  mSearchButton;
    private String api_key = "b000281a6ef7ab7de23ce178afd6faf3";
    private String barcode;

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
            Log.d("response", scanResult.toString());
            String url = "http://api.upcdatabase.org/json/" + api_key + "/" + scanResult.getContents();
            barcode = scanResult.getContents();
            Log.d("Trying to get this", url);
//            new GetHTTPBarcode().execute(url);
            GetBarcode task = new GetBarcode(this);
            task.execute(url);
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
                    // Finds address when user clicks search
                    List<Address> addresses = getCoord(mLocation.getText().toString(), MainSearch.this);
                    Address address;
                    double lat, lon;
                    if (addresses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Address not found", Toast.LENGTH_LONG).show();
                        Log.d("not found!", "address not found!!!");
                        return;
                    }
                    address = addresses.get(0);
                    if (!address.hasLatitude() || !address.hasLongitude()) {
                        Toast.makeText(getApplicationContext(), "Address not found", Toast.LENGTH_LONG).show();
                        Log.d("not found!", "address not found!!!");
                        return;
                    }
                    lat = address.getLatitude();
                    lon = address.getLongitude();


                    Intent intent = new Intent(getApplication(), SearchResults.class);

                    // Set the name of the item
                    intent.putExtra("itemName", "Juice");
                    intent.putExtra("barcode", barcode);
                    // Set the location & radius
                    intent.putExtra("latitude", 35.28304);
                    intent.putExtra("longitude", -120.65925);
                    intent.putExtra("radius", 10.0);
                    startActivity(intent);
                }
            }
        });
    }

    public void postResult(JSONObject result) {
        try {
            String name = result.get("itemname").toString();
            String description = result.get("description").toString();
            if (!name.isEmpty()) {
                mFindItem.setText(name);
                barcode = result.get("number").toString();
            }
            else {
                Toast.makeText(getApplicationContext(), "Item not found in database.", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Item not found in database.", Toast.LENGTH_LONG).show();
            mFindItem.getText().clear();
            barcode = null;
        }
    }

    public List<Address> getCoord(String address, Context context){
        Geocoder gc = new Geocoder(context);
        List<Address> addresses = new ArrayList<Address>();

        try {
            Log.d("looking up", address);
            double[] boundingBox = getBB();
            addresses = gc.getFromLocationName(address, 1, boundingBox[0], boundingBox[3], boundingBox[2], boundingBox[1]);
            for (Address a : addresses) {
                Log.d("address", a.toString());
            }
            Log.d("Addresses", addresses.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public double[] getBB() {
        double[] boundingBox = new double[4];

        double[] myLoc = getGPS();
        boundingBox[0] = myLoc[0] - (5.0/69);
        boundingBox[2] = myLoc[0] + (5.0/69);
        boundingBox[1] = myLoc[1] - (5.0 / (3960 * 2 * Math.PI /360 * Math.cos(boundingBox[0])));
        boundingBox[3] = myLoc[1] + (5.0 / (3960 * 2 * Math.PI /360 * Math.cos(boundingBox[0])));
        return boundingBox;
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }
}
