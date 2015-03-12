package shopstop.grocerylist.datatypes;

import java.math.BigDecimal;

/**
 * Created by Josephine on 2/18/2015.
 */
public class Price {
    private BigDecimal amount;
    private boolean isOnSale;

    private Item item;
    private Store store;

    public Price(String amount, boolean isOnSale, Item item, Store store) {
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

    public Item getItem() {
        return item;
    }

    public Store getStore() {
        return store;
    }
}
