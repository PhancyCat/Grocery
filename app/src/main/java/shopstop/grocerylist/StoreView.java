package shopstop.grocerylist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by dmangin on 2/19/2015.
 */

public class StoreView extends RelativeLayout {
    private TextView mNameView;
    private TextView mAddressView;
    private TextView mDistanceView;
    private TextView mMinPriceView;

    public static StoreView inflate(ViewGroup parent) {
        StoreView itemView = (StoreView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_view, parent, false);
        return itemView;
    }

    public StoreView(Context c) {
        this(c, null);
    }

    public StoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoreView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.store_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        mNameView = (TextView) findViewById(R.id.store_NameTextView);
        mAddressView = (TextView) findViewById(R.id.store_AddressTextView);
        mDistanceView = (TextView) findViewById(R.id.store_DistanceTextView);
        mMinPriceView = (TextView) findViewById(R.id.store_MinPriceTextView);
    }

    public void setItem(Store item) {
        mNameView.setText(item.getName());
        mAddressView.setText(item.getAddress());
        mDistanceView.setText(String.format("%.1f mi", item.getDistance()));
        mMinPriceView.setText(String.format("$%.2f", item.getMinPrice()));
    }

    public TextView getNameView() {
        return mNameView;
    }

    public TextView getAddressView() {
        return mAddressView;
    }

    public TextView getDistanceView() {
        return mDistanceView;
    }
}

