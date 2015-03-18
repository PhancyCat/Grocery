package shopstop.grocerylist.parse;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.apache.commons.lang.WordUtils;

import java.math.BigDecimal;

/**
 * Created by Josephine on 2/18/2015.
 */
public class ParseStore {
    private String name;
    private String address;
    private ParseGeoPoint coordinate;
    private double distance;
    private BigDecimal minPrice;

    public ParseStore(String name, String address, ParseGeoPoint coordinate) {
        this.name = WordUtils.capitalizeFully(name);
        this.address = WordUtils.capitalizeFully(address);
        this.coordinate = coordinate;
    }

    public ParseStore(ParseObject parseObject, ParseGeoPoint origin) {
        this.name = parseObject.getString("name");
        this.address = parseObject.getString("address");
        this.coordinate = parseObject.getParseGeoPoint("coordinate");
        this.distance = parseObject.getParseGeoPoint("coordinate").distanceInMilesTo(origin);
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public ParseGeoPoint getCoordinate() {
        return coordinate;
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
                coordinate.toString().equals(other.coordinate.toString());
    }

    @Override
    public int hashCode() {
        return (name + coordinate.toString()).hashCode();
    }
}
