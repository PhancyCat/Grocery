package shopstop.grocerylist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * Created by dmangin on 2/19/2015.
 */
public class ItemView extends RelativeLayout {
    private TextView mNameView;
    private TextView mQuantityView;
    private TextView mPriceView;
    private TextView mPerUnitView;

    public static ItemView inflate(ViewGroup parent) {
        ItemView itemView = (ItemView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        return itemView;
    }

    public ItemView(Context c) {
        this(c, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.item_view_children, this, true);
        setupChildren();
    }

    private void setupChildren() {
        mNameView = (TextView) findViewById(R.id.item_NameTextView);
        mQuantityView = (TextView) findViewById(R.id.item_QuantityTextView);
        mPriceView = (TextView) findViewById(R.id.item_PriceTextView);
        mPerUnitView = (TextView) findViewById(R.id.item_PerUnitTextView);
    }

    public void setItem(Item item) {
        double perUnitPrice = item.getPrice().doubleValue() / item.getQuantity().doubleValue();
        mNameView.setText(item.getName());
        mQuantityView.setText(item.getQuantity().toString() + " " + item.getUnit());
        mPriceView.setText(String.format("$%.2f", item.getPrice()));
        mPerUnitView.setText(String.format("$%.2f/%s", perUnitPrice, item.getUnit()));
    }

    public TextView getNameView() {
        return mNameView;
    }

    public TextView getAddressView() {
        return mQuantityView;
    }

    public TextView getDistanceView() {
        return mPriceView;
    }
}

