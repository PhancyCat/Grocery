package shopstop.grocerylist.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import shopstop.grocerylist.parse.ParseObjectHandler;
import shopstop.grocerylist.parse.ParseStore;

/**
 * Created by Josephine on 3/11/2015.
 */
public class AddStoreTask extends AsyncTask<String, String, String> {
    ParseObjectHandler handler;
    ParseStore store;

    public AddStoreTask(ParseObjectHandler handler, ParseStore store) {
        this.handler = handler;
        this.store = store;
    }

    @Override
    protected String doInBackground(String... params) {
        ParseQuery<ParseObject> storeQuery = ParseQuery.getQuery("Store");

        storeQuery.whereEqualTo("name", store.getName());
        storeQuery.whereEqualTo("coordinate", store.getCoordinate());

        storeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    System.err.println("Store found in database");
                    handler.onCallComplete(parseObject);
                }
                else {
                    // If store does not exist, create it
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        final ParseObject storeObject = ParseObject.create("Store");

                        storeObject.put("name", store.getName());
                        storeObject.put("address", store.getAddress());
                        storeObject.put("coordinate", store.getCoordinate());

                        storeObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    System.err.println("Store added to database");
                                    handler.onCallComplete(storeObject);
                                }
                                else {
                                    Log.d("store", "Error: " + e.getMessage());
                                }
                            }
                        });
                    }
                    else {
                        Log.d("store", "Error: " + e.getMessage());
                    }
                }
            }
        });

        return null;
    }
}
