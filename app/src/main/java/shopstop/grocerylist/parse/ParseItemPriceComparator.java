package shopstop.grocerylist.parse;

import java.util.Comparator;

/**
 * Created by Josephine on 3/13/2015.
 */
public class ParseItemPriceComparator implements Comparator<ParseItem> {
    @Override
    public int compare(ParseItem lhs, ParseItem rhs) {
        if (lhs.getPrice().compareTo(rhs.getPrice()) < 0) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
