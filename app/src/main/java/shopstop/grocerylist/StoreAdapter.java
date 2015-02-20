package shopstop.grocerylist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by dmangin on 2/19/2015.
 */
public class StoreAdapter extends ArrayAdapter<Store> {
    public StoreAdapter(Context c, List<Store> items) {
        super(c, 0, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreView itemView = (StoreView)convertView;
        if (null == itemView)
            itemView = StoreView.inflate(parent);
        itemView.setItem(getItem(position));
        return itemView;
    }
}
