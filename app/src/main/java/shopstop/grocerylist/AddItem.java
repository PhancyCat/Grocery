package shopstop.grocerylist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseObject;

import shopstop.grocerylist.datatypes.Item;
import shopstop.grocerylist.datatypes.Price;
import shopstop.grocerylist.datatypes.Store;
import shopstop.grocerylist.tasks.AddTask;
import shopstop.grocerylist.tasks.SearchTask;


public class AddItem extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTitle("Add Price");

        final ParseObjectHandler handler = new ParseObjectHandler() {
            @Override
            public void onCallComplete(ParseObject parseObject) {
//                Intent intent = new Intent(getApplication(), MainSearch.class);
//                startActivity(intent);
                finish();
            }
        };

        Button searchButton = (Button) findViewById(R.id.addButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Store store = new Store("Trader Joe's", "3977 S Higuera St, San Luis Obispo, CA 93401");
                Item item = new Item("Juice", "oz", 12.0);
                Price price = new Price(3, 99, false, item, store);

                // Add the price
                AddTask task = new AddTask(handler, price);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
            return rootView;
        }
    }
}
