package shopstop.grocerylist;

import java.math.BigDecimal;

/**
 * Created by dmangin on 2/19/2015.
 */
public class Item {
    private String Name;
    private BigDecimal Quantity;
    private String Unit;
    private BigDecimal Price;

    public Item(String n, BigDecimal a, String u, BigDecimal d) {
        Name = n;
        Quantity = a;
        Unit = u;
        Price = d;
    }

    public String getName() {
        return Name;
    }

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public BigDecimal getPrice() {
        return Price;
    }
}
