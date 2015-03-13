package shopstop.grocerylist.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import shopstop.grocerylist.parse.ParseItem;
import shopstop.grocerylist.parse.ParseQueryHandler;
import shopstop.grocerylist.parse.ParseStore;

/**
 * Created by Josephine on 3/12/2015.
 */
public class SearchStoreTask extends AsyncTask<String, String, String> {
    private ParseQueryHandler handler;
    private String itemName;
    private String storeName;
    private String storeAddress;

    public SearchStoreTask(ParseQueryHandler handler, String itemName, String storeName,
                           String storeAddress) {
        this.handler = handler;
        this.itemName = itemName;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    @Override
    protected String doInBackground(String... params) {
        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");
        itemQuery.whereContainsAll("tags", ParseItem.getTags(itemName));

        ParseQuery<ParseObject> storeQuery = ParseQuery.getQuery("Store");
        storeQuery.whereEqualTo("name", storeName);
        storeQuery.whereEqualTo("address", storeAddress);

        ParseQuery<ParseObject> priceQuery = ParseQuery.getQuery("Price");
        priceQuery.whereMatchesQuery("item", itemQuery);
        priceQuery.whereMatchesQuery("store", storeQuery);
        priceQuery.orderByDescending("createdAt");

        priceQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    System.err.println("Found " + parseObjects.size() + " prices in database");
                    handler.onCallComplete(parseObjects);
                }
                else {
                    Log.d("price", "Error: " + e.getMessage());
                }
            }
        });
        return null;
    }
}
