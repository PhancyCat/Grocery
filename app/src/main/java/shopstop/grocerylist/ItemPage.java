package shopstop.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemPage extends ActionBarActivity {

    private TextView mItem;
    private TextView mPrice;
    private TextView mQuant;
    private TextView mUnit;
    private TextView mStore;
    private TextView mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), StoreMap.class);

                intent.putExtra("lat", getIntent().getDoubleExtra("lat", 0));
                intent.putExtra("lon", getIntent().getDoubleExtra("lon", 0));
                intent.putExtra("name", getIntent().getStringExtra("name"));

                startActivity(intent);
            }
        });

        mItem = (EditText)findViewById(R.id.itemEdit);
        mPrice = (EditText)findViewById(R.id.priceEdit);
        mQuant = (EditText)findViewById(R.id.quantEdit);
        mUnit = (EditText)findViewById(R.id.unitEdit);
        mStore = (EditText)findViewById(R.id.storeEdit);
        mAddress = (EditText)findViewById(R.id.addressEdit);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_page, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
            View rootView = inflater.inflate(R.layout.fragment_item_page, container, false);
            return rootView;
        }
    }
}
