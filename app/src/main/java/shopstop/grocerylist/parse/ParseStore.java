package shopstop.grocerylist.parse;

import com.parse.ParseObject;

/**
 * Created by Josephine on 2/18/2015.
 */
public class ParseStore {
    private String name;
    private String address;

    public ParseStore(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public ParseStore(ParseObject parseObject) {
        this.name = parseObject.getString("name");
        this.address = parseObject.getString("address");
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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
