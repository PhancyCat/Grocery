package shopstop.grocerylist;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ListView;

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
import shopstop.grocerylist.tasks.SearchStoreTask;

public class StoreResults extends ActionBarActivity {
    double lat;
    double lon;

    private Button mMapStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_results);

        mMapStore = (Button)findViewById(R.id.map_icon);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }



        setListeners();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final Activity act = this;

        final ProgressDialog progress = new ProgressDialog(act);

        final String itemName = getIntent().getStringExtra("itemName");
        final String storeName = getIntent().getStringExtra("storeName");
        final String storeAddress = getIntent().getStringExtra("storeAddress");

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

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i("HelloListView", "You clicked ParseItem: " + id + " at position:" + position);
                        // Then you start a new Activity via Intent
                        Intent intent = new Intent(listView.getContext(), ItemPage.class);
                        intent.putExtra("position", position);
                        // Or / And
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });


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

    public void setListeners() {

    }
}
