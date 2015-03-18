package shopstop.grocerylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shopstop.grocerylist.parse.ParseItem;
import shopstop.grocerylist.parse.ParseItemComparator;
import shopstop.grocerylist.parse.ParseQueryHandler;
import shopstop.grocerylist.tasks.Geocoding;
import shopstop.grocerylist.tasks.SearchStoreTask;

public class StoreResults extends ActionBarActivity {
    double lat;
    double lon;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_results);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final Activity act = this;

        final ProgressDialog progress = new ProgressDialog(act);

        final String itemName = getIntent().getStringExtra("itemName");
        final String storeName = getIntent().getStringExtra("storeName");
        final String storeAddress = getIntent().getStringExtra("storeAddress");
        lat = getIntent().getDoubleExtra("lat", 0);
        lon = getIntent().getDoubleExtra("lon", 0);
        name = storeName;

        getSupportActionBar().setTitle(storeName);
        getSupportActionBar().setSubtitle(storeAddress);

        ParseQueryHandler handler = new ParseQueryHandler() {
            @Override
            public void onCallComplete(List<ParseObject> parseObjects) {
                System.err.println("----- call complete -----");

                List<ParseItem> results = groupResults(parseObjects);
                List<Item> items = new ArrayList<>();
                
                Collections.sort(results, new ParseItemComparator());

                // Add items to list
                for (ParseItem item : results) {
                    items.add(new Item(item.getName(), item.getUnitCount(), item.getUnitName(),
                            item.getPrice()));
                }

                final ListView listView = (ListView) findViewById(R.id.listview_store_results);

                listView.setAdapter(new ItemAdapter(act, items));

                progress.dismiss();
            }
        };

        progress.setMessage("Loading items...");
        progress.show();

        // Start the query
        SearchStoreTask task = new SearchStoreTask(handler, itemName, storeName, storeAddress);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private List<ParseItem> groupResults(List<ParseObject> parseObjects) {
        List<ParseItem> results = new ArrayList<>();
        Set<ParseItem> itemSet = new HashSet<>();

        try {
            for (ParseObject price : parseObjects) {
                ParseObject itemObject = price.getParseObject("item");
                itemObject.fetchIfNeeded();
                ParseItem item = new ParseItem(itemObject);

                if (!itemSet.contains(item)) {
                    itemSet.add(item);
                    item.setPrice(new BigDecimal(price.getString("amount")));
                    results.add(item);
                }
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.map_icon) {
            Intent intent = new Intent(getApplication(), StoreMap.class);

            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            intent.putExtra("name", name);

            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }
        private ArrayAdapter<String> adapter;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_store_results, container, false);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.leftin, R.animator.leftout);

        return;
    }
}
