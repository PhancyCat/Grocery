package shopstop.grocerylist.parse;

import java.math.BigDecimal;

/**
 * Created by Josephine on 2/18/2015.
 */
public class ParsePrice {
    private BigDecimal amount;

    private ParseItem item;
    private ParseStore store;

    public ParsePrice(String amount, ParseItem item, ParseStore store) {
        this.amount = new BigDecimal(amount);
        this.item = item;
        this.store = store;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ParseItem getItem() {
        return item;
    }

    public ParseStore getStore() {
        return store;
    }
}
