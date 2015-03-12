package shopstop.grocerylist.datatypes;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> tags() {
        return Item.tags(this.name);
    }

    public static List<String> tags(String name) {
        List<String> list = new ArrayList<String>();
        for (String tag : name.toLowerCase().split(" ")) {
            list.add(tag.trim());
        }
        return list;
    }
}
