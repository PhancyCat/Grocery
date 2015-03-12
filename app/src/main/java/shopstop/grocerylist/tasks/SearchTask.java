package shopstop.grocerylist.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import shopstop.grocerylist.parse.ParseQueryHandler;
import shopstop.grocerylist.parse.ParseItem;

/**
 * Created by Josephine on 2/19/2015.
 */
public class SearchTask extends AsyncTask<String, String, String> {
    private ParseQueryHandler handler;
    private String itemName;
    private ParseGeoPoint coordinate;
    private Double radius;

    public SearchTask(ParseQueryHandler handler, String itemName, ParseGeoPoint coordinate,
                      Double radius) {
        this.handler = handler;
        this.itemName = itemName;
        this.coordinate = coordinate;
        this.radius = radius;
    }

    @Override
    protected String doInBackground(String... params) {
        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");

        itemQuery.whereContainsAll("tags", ParseItem.getTags(itemName));

        ParseQuery<ParseObject> priceQuery = ParseQuery.getQuery("Price");
        priceQuery.whereMatchesQuery("item", itemQuery);

        if (coordinate != null) {
            ParseQuery<ParseObject> storeQuery = ParseQuery.getQuery("Store");
            storeQuery.whereWithinMiles("coordinate", coordinate, radius);
            priceQuery.whereMatchesQuery("store", storeQuery);
        }

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
