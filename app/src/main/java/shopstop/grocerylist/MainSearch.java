package shopstop.grocerylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.Parse;
import com.parse.ParseObject;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.List;

import shopstop.grocerylist.parse.ParseObjectHandler;
import shopstop.grocerylist.tasks.Geocoding;
import shopstop.grocerylist.tasks.GetBarcode;
import shopstop.grocerylist.tasks.HTTPResponse;
import shopstop.grocerylist.tasks.SearchBarcodeTask;

public class MainSearch extends ActionBarActivity implements HTTPResponse,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private EditText mFindItem;
    private EditText mLocation;
    private EditText mDistance;
    private Button  mSearchButton;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MainSearch.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private String api_key = "b000281a6ef7ab7de23ce178afd6faf3";
    private String barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("ShopStop Search");
        actionBar.setIcon(R.drawable.shopstopicon); // this doesn't work...

        mFindItem = (EditText)findViewById(R.id.findItemName);
        mLocation = (EditText)findViewById(R.id.location);
        mDistance = (EditText)findViewById(R.id.disance);
        mSearchButton = (Button) findViewById(R.id.searchButton);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


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

//        final Button scanButton = (Button) findViewById(R.id.barcode);
//        scanButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                IntentIntegrator integrator = new IntentIntegrator(act);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setPrompt("Scan a barcode");
//                integrator.setResultDisplayDuration(0);
//                integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
//                integrator.setCameraId(0);  // Use a specific camera of the device
//                integrator.initiateScan();
//            }
//        });
//
//        Button addButton = (Button) findViewById(R.id.addPrice);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplication(), AddItem.class);
//                startActivity(intent);
//            }
//        });

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
        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplication(), AddItem.class);
            startActivity(intent);

            overridePendingTransition(R.animator.downin, R.animator.downout);
        }

        if (id == R.id.action_scan) {
            IntentIntegrator integrator = new IntentIntegrator(MainSearch.this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setResultDisplayDuration(0);
            integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.initiateScan();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
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

        final Activity act = this;

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
                    mFindItem.setError("Only alphanumeric and special characters: .,()- are allowed!");
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
                    mDistance.setError(mDistance.getHint() + " is required!");
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
                    mLocation.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mLocation.setError(null);
                } else if (!str.matches("[a-zA-Z0-9., ]++")) {
                    mLocation.setError("Only alphanumeric and special characters: ., are allowed!");
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
                        mDistance.getText().toString().equals("")|| mDistance.getError() != null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(act).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please check that you filled in the fields correctly.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    // Finds address when user clicks search
                    Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    double lat, lon;
                    Geocoding gc = new Geocoding(MainSearch.this);
                    if(!(mLocation.getText().toString().equals("") || mLocation.getError() != null)) {
                        List<Address> addresses = gc.getCoord(mLocation.getText().toString(), MainSearch.this);
                        Address address;
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
                    }
                    else {
                        lat = gc.getGPS()[0];
                        lon = gc.getGPS()[1];
                    }

                    Intent intent = new Intent(getApplication(), SearchResults.class);

                    // Set the name of the item
                    intent.putExtra("itemName", mFindItem.getText().toString());
                    intent.putExtra("location", mLocation.getText().toString());
                    intent.putExtra("barcode", barcode);
                    // Set the location & radius
                    intent.putExtra("latitude", lat);
                    intent.putExtra("longitude", lon);
                    intent.putExtra("radius", Double.parseDouble(mDistance.getText().toString()));

                    startActivity(intent);

                    overridePendingTransition(R.animator.upin, R.animator.upout);
                }
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

    public void postResult(JSONObject result) {
        if (barcode != null) {
            final ProgressDialog progress = new ProgressDialog(this);

            // Now search Parse for the item
            ParseObjectHandler handler = new ParseObjectHandler() {
                @Override
                public void onCallComplete(ParseObject parseObject) {
                    progress.dismiss();

                    if (parseObject == null) {
                        Toast.makeText(getApplicationContext(), "Item not found in database.", Toast.LENGTH_LONG).show();
                    } else {
                        mFindItem.setText(parseObject.getString("name"));
                    }
                }
            };

            progress.setMessage("Searching for item...");
            progress.show();

            SearchBarcodeTask parseTask = new SearchBarcodeTask(handler, barcode);
            parseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

}
