package shopstop.grocerylist.parse;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Josephine on 2/19/2015.
 */
public abstract class ParseQueryHandler {
    public abstract void onCallComplete(List<ParseObject> parseObjects);
}
