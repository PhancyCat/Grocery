package shopstop.grocerylist.parse;

import java.util.Comparator;

/**
 * Created by Josephine on 3/12/2015.
 */
public class ParseStoreMinPriceComparator implements Comparator<ParseStore> {
    @Override
    public int compare(ParseStore lhs, ParseStore rhs) {
        if (lhs.getMinPrice().compareTo(rhs.getMinPrice()) < 0) {
            return -1;
        }
        else {
            return 1;
        }
    }
}
