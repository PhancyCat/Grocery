package shopstop.grocerylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.zxing.integration.android.*;
import com.parse.Parse;



public class MainSearch extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();

        setTitle("Search");

        Bundle bundle = getIntent().getExtras();
        String fillItem = null;

        if(bundle != null) {
            fillItem = bundle.getString("itemName");
            EditText item = (EditText) findViewById(R.id.findItemName);
            item.setText(fillItem);
        } else {
            Log.d("Bundle Check", "Activity was passed nothing");
        }

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

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchResults.class);

                // Set the name of the item
                intent.putExtra("itemName", "Juice");
                // Set the location & radius
                intent.putExtra("latitude", 35.28304);
                intent.putExtra("longitude", -120.65925);
                intent.putExtra("radius", 10.0);

                startActivity(intent);
            }
        });
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
}
