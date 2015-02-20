package shopstop.grocerylist;

/**
 * Created by dmangin on 2/19/2015.
 */
public class Store {
    private String Name;
    private String Address;
    private Double Distance;

    public Store(String n, String a, Double d) {
        Name = n;
        Address = a;
        Distance = d;
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
}
