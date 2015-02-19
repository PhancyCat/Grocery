package shopstop.grocerylist;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        ListView listview = (ListView) findViewById(R.id.listview_results);
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
            final ListView listView = (ListView) rootView.findViewById(R.id.listview_results);
            if (listView != null) {
                listView.setAdapter(adapter);
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
            return rootView;
        }
    }
}
