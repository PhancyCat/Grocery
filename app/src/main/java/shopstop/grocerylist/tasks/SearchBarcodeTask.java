package shopstop.grocerylist.tasks;

import android.os.AsyncTask;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import shopstop.grocerylist.parse.ParseObjectHandler;

/**
 * Created by Josephine on 3/17/2015.
 */
public class SearchBarcodeTask extends AsyncTask<String, String, String> {
    ParseObjectHandler handler;
    String barcode;

    public SearchBarcodeTask(ParseObjectHandler handler, String barcode) {
        this.handler = handler;
        this.barcode = barcode;
    }

    @Override
    protected String doInBackground(String... params) {
        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");

        itemQuery.whereEqualTo("barcode", barcode);

        itemQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    System.err.println("Barcode: Item found in database");
                    handler.onCallComplete(parseObject);
                }
                else {
                    System.err.println("Barcode: Item not found in database");
                    handler.onCallComplete(null);
                }
            }
        });

        return null;
    }
}
