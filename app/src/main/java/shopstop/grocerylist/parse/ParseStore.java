package shopstop.grocerylist.parse;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.math.BigDecimal;

/**
 * Created by Josephine on 2/18/2015.
 */
public class ParseStore {
    private String name;
    private String address;
    private double distance;
    private BigDecimal minPrice;

    public ParseStore(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public ParseStore(ParseObject parseObject, ParseGeoPoint origin) {
        this.name = parseObject.getString("name");
        this.address = parseObject.getString("address");
        this.distance = parseObject.getParseGeoPoint("coordinate").distanceInMilesTo(origin);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getDistance() {
        return distance;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal other) {
        if (minPrice == null || other.compareTo(minPrice) < 0) {
            minPrice = other;
        }
    }

    @Override
    public boolean equals(Object o) {
        ParseStore other = (ParseStore) o;
        return name.equals(other.name) &&
                address.equals(other.address);
    }

    @Override
    public int hashCode() {
        return (name + address).hashCode();
    }
}
