package shopstop.grocerylist;

import java.math.BigDecimal;

/**
 * Created by dmangin on 2/19/2015.
 */
public class Store {
    private String Name;
    private String Address;
    private Double Distance;
    private BigDecimal MinPrice;

    public Store(String n, String a, Double d, BigDecimal m) {
        Name = n;
        Address = a;
        Distance = d;
        MinPrice = m;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public Double getDistance() {
        return Distance;
    }

    public BigDecimal getMinPrice() {
        return MinPrice;
    }
}
