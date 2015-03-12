package shopstop.grocerylist.datatypes;

import com.parse.ParseObject;

import org.json.JSONArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josephine on 2/18/2015.
 */
public class Item {
    private String name;
    private String unitName;
    private BigDecimal unitCount;

    public Item(String name, String unitName, String unitCount) {
        this.name = name;
        this.unitName = unitName;

        if (unitCount == null) {
            this.unitCount = new BigDecimal("1");
        }
        else {
            this.unitCount = new BigDecimal(unitCount);
        }
    }

    public Item(ParseObject parseObject) {
        this.name = parseObject.getString("name");
        this.unitName = parseObject.getString("unitName");
        this.unitCount = new BigDecimal(parseObject.getString("unitCount"));
    }

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getUnitCount() {
        return unitCount.toString();
    }

    public List<String> getTags() {
        return Item.getTags(this.name);
    }

    public static List<String> getTags(String name) {
        List<String> list = new ArrayList<>();
        for (String tag : name.toLowerCase().split(" ")) {
            list.add(tag.trim());
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        Item other = (Item) o;
        return name.equals(other.name) &&
                unitName.equals(other.unitName) &&
                unitCount.equals(other.unitCount);
    }

    @Override
    public int hashCode() {
        return (name + unitName + unitCount.toString()).hashCode();
    }
}
