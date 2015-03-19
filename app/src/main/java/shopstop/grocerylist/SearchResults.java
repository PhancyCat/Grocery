package shopstop.grocerylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.apache.commons.lang.WordUtils;

import java.math.BigDecimal;
import java.util.*;

import shopstop.grocerylist.parse.*;
import shopstop.grocerylist.tasks.SearchTask;

public class SearchResults extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        ListView listview = (ListView) findViewById(R.id.listview_item_results);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final Activity act = this;

        final ProgressDialog progress = new ProgressDialog(act);

        // Get the item to search for
        final String itemName = getIntent().getStringExtra("itemName");
        final String location = getIntent().getStringExtra("location");
        final double latitude = getIntent().getDoubleExtra("latitude", 0);
        final double longitude = getIntent().getDoubleExtra("longitude", 0);
        final double radius = getIntent().getDoubleExtra("radius", 10);

        final ParseGeoPoint coordinate = new ParseGeoPoint(latitude, longitude);

        getSupportActionBar().setTitle(WordUtils.capitalizeFully(itemName));
        getSupportActionBar().setSubtitle("Near: " + location);

        // Handle the query results
        ParseQueryHandler handler = new ParseQueryHandler() {
            @Override
            public void onCallComplete(List<ParseObject> parseObjects) {
                System.err.println("----- call complete -----");

                final List<ParseStore> results = groupResults(parseObjects, coordinate);
                Collections.sort(results, new ParseStoreComparator());

                // Add stores to list
                final List<Store> stores = new ArrayList<>();
                for (ParseStore store : results) {
                    stores.add(new Store(store.getName(), store.getAddress(), store.getDistance(),
                            store.getMinPrice()));
                }

                final ListView listView = (ListView) findViewById(R.id.listview_item_results);

                listView.setAdapter(new StoreAdapter(act, stores));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i("HelloListView", "You clicked ParseItem: " + id + " at position:" + position);

                        Intent intent = new Intent(listView.getContext(), StoreResults.class);

                        intent.putExtra("itemName", itemName);
                        intent.putExtra("storeName", stores.get(position).getName());
                        intent.putExtra("storeAddress", stores.get(position).getAddress());
                        intent.putExtra("lat", results.get(position).getCoordinate().getLatitude());
                        intent.putExtra("lon", results.get(position).getCoordinate().getLongitude());

                        startActivity(intent);

                        overridePendingTransition(R.animator.leftin, R.animator.leftout);
                    }
                });

                progress.dismiss();
                if(stores.isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(act).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("No items found for specified parameters.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        };

        progress.setMessage("Loading stores...");
        progress.show();

        // Start the query
        SearchTask task = new SearchTask(handler, itemName, coordinate, radius);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private List<ParseStore> groupResults(List<ParseObject> parseObjects, ParseGeoPoint origin) {
        List<ParseStore> results = new ArrayList<>();
        Map<ParseStore, Set<ParseItem>> storeMap = new HashMap<>();

        try {
            for (ParseObject price : parseObjects) {
                ParseObject storeObject = price.getParseObject("store");
                storeObject.fetchIfNeeded();
                ParseObject itemObject = price.getParseObject("item");
                itemObject.fetchIfNeeded();

                ParseStore store = new ParseStore(storeObject, origin);
                ParseItem item = new ParseItem(itemObject);

                if (storeMap.containsKey(store)) {
                    Set<ParseItem> itemSet = storeMap.get(store);
                    // We only care about the most recent price for an item
                    // If item is already in history, we already have the most recent price
                    if (!itemSet.contains(item)) {
                        itemSet.add(item); // Add item to history

                        results.get(results.indexOf(store)).setMinPrice(
                                new BigDecimal(price.getString("amount")));
                    }
                }
                else {
                    Set<ParseItem> itemSet = new HashSet<>();
                    itemSet.add(item); // Add item to history
                    storeMap.put(store, itemSet);

                    store.setMinPrice(new BigDecimal(price.getString("amount")));
                    results.add(store);
                }
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        return results;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.upin, R.animator.upout);

        return;
    }
}
