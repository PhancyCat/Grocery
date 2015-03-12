package shopstop.grocerylist.parse;

import com.parse.ParseObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josephine on 2/18/2015.
 */
public class ParseItem {
    private String name;
    private String unitName;
    private BigDecimal unitCount;

    public ParseItem(String name, String unitName, String unitCount) {
        this.name = name;
        this.unitName = unitName;

        if (unitCount == null) {
            this.unitCount = new BigDecimal("1");
        }
        else {
            this.unitCount = new BigDecimal(unitCount);
        }
    }

    public ParseItem(ParseObject parseObject) {
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
        return ParseItem.getTags(this.name);
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
        ParseItem other = (ParseItem) o;
        return name.equals(other.name) &&
                unitName.equals(other.unitName) &&
                unitCount.equals(other.unitCount);
    }

    @Override
    public int hashCode() {
        return (name + unitName + unitCount.toString()).hashCode();
    }
}
