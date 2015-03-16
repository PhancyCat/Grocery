package shopstop.grocerylist.tasks;

import org.json.JSONObject;

/**
 * Created by bsyu on 3/15/15.
 */
public interface HTTPResponse {
    void postResult(JSONObject result);
}