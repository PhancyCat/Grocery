package shopstop.grocerylist.parse;

import com.parse.ParseObject;

import org.apache.commons.lang.WordUtils;

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
    private BigDecimal price;
    private String barcode;

    public ParseItem(String name, String unitName, String unitCount, String barcode) {
        this.name = WordUtils.capitalizeFully(name);

        if (unitName == null) {
            this.unitName = "unit";
        }
        else {
            this.unitName = unitName.toLowerCase();
        }

        if (unitCount == null) {
            this.unitCount = new BigDecimal("1");
        }
        else {
            this.unitCount = new BigDecimal(unitCount);
        }

        this.barcode = barcode;
    }

    public ParseItem(ParseObject parseObject) {
        this.name = parseObject.getString("name");
        this.unitName = parseObject.getString("unitName");
        this.unitCount = new BigDecimal(parseObject.getString("unitCount"));
        this.barcode = parseObject.getString("barcode");
    }

    public String getName() {
        return name;
    }

    public String getUnitName() {
        return unitName;
    }

    public BigDecimal getUnitCount() {
        return unitCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getBarcode() {
        return barcode;
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

    public void setPrice(BigDecimal other) {
        if (price == null || other.compareTo(price) < 0) {
            price = other;
        }
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
