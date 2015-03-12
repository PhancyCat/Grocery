package shopstop.grocerylist.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import shopstop.grocerylist.ParseObjectHandler;
import shopstop.grocerylist.datatypes.Price;

/**
 * Created by Josephine on 2/19/2015.
 */
public class AddPriceTask extends AsyncTask<String, String, String> {
    private ParseObjectHandler handler;
    private Price price;

    public AddPriceTask(ParseObjectHandler handler, Price price) {
        this.handler = handler;
        this.price = price;
    }

    @Override
    protected String doInBackground(String... params) {
        final ParseObject priceObject = ParseObject.create("Price");

        priceObject.put("dollars", price.dollars);
        priceObject.put("cents", price.cents);
        priceObject.put("isOnSale", price.isOnSale);

        final ParseObjectHandler storeHandler = new ParseObjectHandler() {
            @Override
            public void onCallComplete(ParseObject parseObject) {
                priceObject.put("store", parseObject);

                // Finally, save price to database
                priceObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            System.err.println("Price added to database");
                            handler.onCallComplete(priceObject);
                        }
                        else {
                            Log.d("price", "Error: " + e.getMessage());
                        }
                    }
                });
            }
        };

        final ParseObjectHandler itemHandler = new ParseObjectHandler() {
            @Override
            public void onCallComplete(ParseObject parseObject) {
                priceObject.put("item", parseObject);

                AddStoreTask storeTask = new AddStoreTask(storeHandler, price.store);
                storeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };

        AddItemTask itemTask = new AddItemTask(itemHandler, price.item);
        itemTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return null;
    }
}
