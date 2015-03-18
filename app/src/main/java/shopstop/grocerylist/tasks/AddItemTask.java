package shopstop.grocerylist.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import shopstop.grocerylist.parse.ParseObjectHandler;
import shopstop.grocerylist.parse.ParseItem;

/**
 * Created by Josephine on 3/11/2015.
 */
public class AddItemTask extends AsyncTask<String, String, String> {
    private ParseObjectHandler handler;
    private ParseItem item;

    public AddItemTask(ParseObjectHandler handler, ParseItem item) {
        this.handler = handler;
        this.item = item;
    }

    @Override
    protected String doInBackground(String... params) {
        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");

        itemQuery.whereEqualTo("name", item.getName());
        itemQuery.whereEqualTo("unitName", item.getUnitName());
        itemQuery.whereEqualTo("unitCount", item.getUnitCount().toString());

        itemQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    System.err.println("Item found in database");
                    handler.onCallComplete(parseObject);
                }
                else {
                    // If item does not exist, create it
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        final ParseObject itemObject = ParseObject.create("Item");

                        itemObject.put("name", item.getName());
                        itemObject.put("tags", item.getTags());
                        itemObject.put("unitName", item.getUnitName());
                        itemObject.put("unitCount", item.getUnitCount().toString());

                        itemObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    System.err.println("Item added to database");
                                    handler.onCallComplete(itemObject);
                                }
                                else {
                                    Log.d("item", "Error: " + e.getMessage());
                                }
                            }
                        });
                    }
                    else {
                        Log.d("item", "Error: " + e.getMessage());
                    }
                }
            }
        });

        return null;
    }
}
