package shopstop.grocerylist.datatypes;

/**
 * Created by Josephine on 2/18/2015.
 */
public class Price {
    public int dollars;
    public int cents;
    public boolean isOnSale;
    public Item item;
    public Store store;

    public Price(int dollars, int cents, boolean isOnSale, Item item, Store store) {
        this.dollars = dollars;
        this.cents = cents;
        this.isOnSale = isOnSale;
        this.item = item;
        this.store = store;
    }
}
