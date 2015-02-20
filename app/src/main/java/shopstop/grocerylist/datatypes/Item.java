package shopstop.grocerylist.datatypes;

/**
 * Created by Josephine on 2/18/2015.
 */
public class Item {
    public String name;
    public String unitName;
    public double unitCount;

    public Item(String name, String unitName, double unitCount) {
        this.name = name;
        this.unitName = unitName;
        this.unitCount = unitCount;
    }
}
