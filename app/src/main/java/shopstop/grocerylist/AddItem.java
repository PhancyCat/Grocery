package shopstop.grocerylist;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;

import shopstop.grocerylist.parse.ParseItem;
import shopstop.grocerylist.parse.ParseObjectHandler;
import shopstop.grocerylist.parse.ParsePrice;
import shopstop.grocerylist.parse.ParseStore;
import shopstop.grocerylist.tasks.AddPriceTask;


public class AddItem extends Activity {

    private EditText mItem;
    private EditText mPrice;
    private EditText mQuant;
    private EditText mStore;
    private EditText mAddress;
    private Button mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTitle("Add Price");

        mItem = (EditText)findViewById(R.id.itemEdit);
        mPrice = (EditText)findViewById(R.id.priceEdit);
        mQuant = (EditText)findViewById(R.id.quantEdit);
        mStore = (EditText)findViewById(R.id.storeEdit);
        mAddress = (EditText)findViewById(R.id.addressEdit);
        mAddButton = (Button) findViewById(R.id.addButton);

        setListeners();

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

    private void setListeners() {
        mItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mItem.getText().toString();

                if(mItem.getText().toString().equals("")) {
                    mItem.setError("Input is required!");
                    mItem.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("[a-zA-Z0-9 ]++")) {
                    mItem.setError("Only alphanumeric characters are allowed!");
                    mItem.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mItem.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mItem.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mPrice.getText().toString();

                if(mPrice.getText().toString().equals("")) {
                    mPrice.setError("Input is required!");
                    mPrice.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("\\d+[.]?\\d*")) {
                    mPrice.setError("Only numbers are allowed!");
                    mPrice.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mPrice.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mPrice.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mQuant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mQuant.getText().toString();

                if(mQuant.getText().toString().equals("")) {
                    mQuant.setError("Input is required!");
                    mQuant.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("\\d+")) {
                    mQuant.setError("Only whole numbers are allowed!");
                    mQuant.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mQuant.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mQuant.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mStore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mStore.getText().toString();

                if(mStore.getText().toString().equals("")) {
                    mStore.setError("Input is required!");
                    mStore.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("[a-zA-Z0-9 ]++")) {
                    mStore.setError("Only alphanumeric characters are allowed!");
                    mStore.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mStore.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mStore.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = mAddress.getText().toString();

                if(mAddress.getText().toString().equals("")) {
                    mAddress.setError("Input is required!");
                    mAddress.setBackgroundColor(getResources().getColor(R.color.transred));
                } else if (!str.matches("[a-zA-Z0-9 ]++")) {
                    mAddress.setError("Only alphanumeric characters are allowed!");
                    mAddress.setBackgroundColor(getResources().getColor(R.color.transred));
                } else  {
                    mAddress.setBackgroundColor(getResources().getColor(R.color.transgreen));
                    mAddress.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final ParseObjectHandler handler = new ParseObjectHandler() {
            @Override
            public void onCallComplete(ParseObject parseObject) {
                finish();
            }
        };
        mAddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mItem.getText().toString().equals("") || mItem.getError() != null ||
                        mPrice.getText().toString().equals("") || mPrice.getError() != null ||
                        mQuant.getText().toString().equals("") || mQuant.getError() != null ||
                        mStore.getText().toString().equals("") || mStore.getError() != null ||
                        mAddress.getText().toString().equals("") || mAddress.getError() != null) {
                    ;
                } else {
                    ParseStore store = new ParseStore("Trader Joe's", "3977 S Higuera St, San Luis Obispo, CA 93401");
                    ParseItem item = new ParseItem("Juice", "oz", "12.0");
                    ParsePrice price = new ParsePrice("3.99", false, item, store);

                    // Add the price
                    AddPriceTask task = new AddPriceTask(handler, price);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
    }
}
