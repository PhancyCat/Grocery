package shopstop.grocerylist.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import shopstop.grocerylist.ParseObjectHandler;
import shopstop.grocerylist.ParseQueryHandler;
import shopstop.grocerylist.datatypes.Price;

/**
 * Created by Josephine on 2/19/2015.
 */
public class AddTask extends AsyncTask<String, String, String> {
    private ParseObjectHandler handler;
    private Price price;

    public AddTask(ParseObjectHandler handler, Price price) {
        this.handler = handler;
        this.price = price;
    }

    @Override
    protected String doInBackground(String... params) {
        final ParseObject priceObject = ParseObject.create("Price");

        priceObject.put("storeName", price.store.name);
        priceObject.put("storeAddress", price.store.address);
        priceObject.put("itemName", price.item.name);
        priceObject.put("itemUnitName", price.item.unitName);
        priceObject.put("itemUnitCount", price.item.unitCount);
        priceObject.put("dollars", price.dollars);
        priceObject.put("cents", price.cents);
        priceObject.put("isOnSale", price.isOnSale);

        priceObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.err.println("Price added to database");
                    handler.onCallComplete(priceObject);

                    addItem(price, priceObject);
                } else {
                    Log.d("price", "Error: " + e.getMessage());
                }
            }
        });
        return null;
    }

    private void addItem(final Price price, final ParseObject priceObject) {
        ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("Item");

        itemQuery.whereEqualTo("name", price.item.name);
        itemQuery.whereEqualTo("unitName", price.item.unitName);
        itemQuery.whereEqualTo("unitCount", price.item.unitCount);
        itemQuery.setLimit(1);

        itemQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    // Create item if it does not exist
                    if (parseObjects.size() == 0) {
                        final ParseObject itemObject = ParseObject.create("Item");

                        itemObject.put("name", price.item.name);
                        itemObject.put("unitName", price.item.unitName);
                        itemObject.put("unitCount", price.item.unitCount);
                        itemObject.getRelation("prices").add(priceObject);

                        itemObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                System.err.println("Item added to database");

                                addStore(price, itemObject);
                            }
                        });
                    }
                }
                else {
                    Log.d("item", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void addStore(final Price price, final ParseObject itemObject) {
        ParseQuery<ParseObject> storeQuery = ParseQuery.getQuery("Store");

        storeQuery.whereEqualTo("name", price.store.name);
        storeQuery.whereEqualTo("address", price.store.address);
        storeQuery.setLimit(1);

        storeQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    // Create store if it does not exist
                    if (parseObjects.size() == 0) {
                        ParseObject newStore = ParseObject.create("Store");

                        newStore.put("name", price.store.name);
                        newStore.put("address", price.store.address);
                        // TODO: Geocoding
                        newStore.getRelation("items").add(itemObject);

                        newStore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                System.err.println("Store added to database");
                            }
                        });
                    }
                }
                else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
    }

}
