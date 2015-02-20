package shopstop.grocerylist.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import shopstop.grocerylist.ParseQueryHandler;

/**
 * Created by Josephine on 2/19/2015.
 */
public class SearchTask extends AsyncTask<String, String, String> {
    private ParseQueryHandler handler;
    private String itemName;

    public SearchTask(ParseQueryHandler handler, String itemName) {
        this.handler = handler;
        this.itemName = itemName;
    }

    @Override
    protected String doInBackground(String... params) {
        ParseQuery<ParseObject> priceQuery = ParseQuery.getQuery("Price");
        priceQuery.whereEqualTo("itemName", itemName);

        ParseQuery<ParseObject> storeQuery = ParseQuery.getQuery("Store");
        storeQuery.whereMatchesKeyInQuery("name", "storeName", priceQuery);
        storeQuery.whereMatchesKeyInQuery("address", "storeAddress", priceQuery);

        storeQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    System.err.println("Done searching");
                    handler.onCallComplete(stores);
                }
                else {

                }
            }
        });
        return null;
    }
}
