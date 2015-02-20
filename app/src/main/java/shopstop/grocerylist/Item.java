package shopstop.grocerylist;

/**
 * Created by dmangin on 2/19/2015.
 */
public class Item {
    private String Name;
    private Integer Quantity;
    private Double Price;

    public Item(String n, Integer a, Double d) {
        Name = n;
        Quantity = a;
        Price = d;
    }

    public String getName() {
        return Name;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Double getPrice() {
        return Price;
    }
}
