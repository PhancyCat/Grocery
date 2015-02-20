package shopstop.grocerylist;

import android.app.Activity;
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

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import shopstop.grocerylist.tasks.SearchTask;

public class SearchResults extends ActionBarActivity {
    ArrayList<Store> stores = new ArrayList<Store>();

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

        // Get the item to search for
        String itemName = getIntent().getStringExtra("itemName");

        // Handle the query results
        ParseQueryHandler handler = new ParseQueryHandler() {
            @Override
            public void onCallComplete(List<ParseObject> parseObjects) {
                System.err.println("----- call complete -----");

                // Add stores to list
                for (ParseObject store : parseObjects) {
                    stores.add(new Store(store.get("name").toString(), store.get("address").toString(), 0.25));
                }

                // List adapter stuff, change as necessary
                final ListView listView = (ListView) findViewById(R.id.listview_item_results);

                listView.setAdapter(new StoreAdapter(act, stores));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);

                        Intent intent = new Intent(listView.getContext(), StoreResults.class);

                        intent.putExtra("position", position);
                        intent.putExtra("id", id);

                        startActivity(intent);
                    }
                });
            }
        };

        // Start the query
        SearchTask task = new SearchTask(handler, itemName);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

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
            /*
            ArrayList<Store> items = new ArrayList<Store>();

            Store temp = new Store("albertsons", "Here", 0.23);
            items.add(temp);
            temp = new Store("lol", "lol", 3.45);
            items.add(temp);
            String[] data = {
                    "Mon 6/23 - Sunny - 31/17",
                    "Tue 6/24 - Foggy - 21/8",
                    "Wed 6/25 - Cloudy - 22/17",
                    "Thurs 6/26 - Rainy - 18/11",
                    "Fri 6/27 - Foggy - 21/10",
                    "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                    "Sun 6/29 - Sunny - 20/7"
            };
            List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));
            adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_results, R.id.list_item_results_textview, weekForecast);
            final ListView listView = (ListView) rootView.findViewById(R.id.listview_item_results);
            if (listView != null) {
                //listView.setAdapter(adapter);
                listView.setAdapter(new StoreAdapter(getActivity(), items));
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {
                    Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
// Then you start a new Activity via Intent
                    Intent intent = new Intent(listView.getContext(), StoreResults.class);
                    intent.putExtra("position", position);
// Or / And
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            */
            return rootView;
        }
    }
}
