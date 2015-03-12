package shopstop.grocerylist.parse;

import java.math.BigDecimal;

/**
 * Created by Josephine on 2/18/2015.
 */
public class ParsePrice {
    private BigDecimal amount;
    private boolean isOnSale;

    private ParseItem item;
    private ParseStore store;

    public ParsePrice(String amount, boolean isOnSale, ParseItem item, ParseStore store) {
        this.amount = new BigDecimal(amount);
        this.isOnSale = isOnSale;
        this.item = item;
        this.store = store;
    }

    public String getAmount() {
        return amount.toString();
    }

    public boolean getIsOnSale() {
        return isOnSale;
    }

    public ParseItem getItem() {
        return item;
    }

    public ParseStore getStore() {
        return store;
    }
}
