package shopstop.grocerylist.tasks;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bsyu on 3/15/15.
 */
public class Geocoding {

    Context mContext;

    public Geocoding (Context context) {
        this.mContext = context;
    }
    public List<Address> getCoord(String address, Context context){
        Geocoder gc = new Geocoder(context);
        List<Address> addresses = new ArrayList<Address>();

        try {
            Log.d("looking up", address);
            double[] boundingBox = getBB();
            addresses = gc.getFromLocationName(address, 1, boundingBox[0], boundingBox[3], boundingBox[2], boundingBox[1]);
            for (Address a : addresses) {
                Log.d("address", a.toString());
            }
            Log.d("Addresses", addresses.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public double[] getBB() {
        double[] boundingBox = new double[4];

        double[] myLoc = getGPS();
        boundingBox[0] = myLoc[0] - (5.0/69);
        boundingBox[2] = myLoc[0] + (5.0/69);
        boundingBox[1] = myLoc[1] - (5.0 / (3960 * 2 * Math.PI /360 * Math.cos(boundingBox[0])));
        boundingBox[3] = myLoc[1] + (5.0 / (3960 * 2 * Math.PI /360 * Math.cos(boundingBox[0])));
        return boundingBox;
    }

    private double[] getGPS() {
        LocationManager lm = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/
        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

        double[] gps = new double[2];
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }
}
