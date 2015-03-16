package shopstop.grocerylist.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by bsyu on 3/15/15.
 */
public class GetBarcode extends AsyncTask<String, String, JSONObject> {

    public HTTPResponse mlistener=null;

    public GetBarcode(HTTPResponse listener){
        this.mlistener=listener;
    }

    protected JSONObject doInBackground(String ... uri) {
        StringBuilder builder = new StringBuilder();
        JSONObject jsonObject = new JSONObject();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri[0]);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
                jsonObject = new JSONObject(builder.toString());
            }
            else {
                Log.d("Bad error code", "" + statusCode);
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    protected void onProgressUpdate() {
        Log.d("Response", "updating");
    }

    protected void onPostExecute(JSONObject result) {
//        try {
//            String name = result.get("itemname").toString();
//            mFindItem.setText(name);
//        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), "Item not found in database.", Toast.LENGTH_LONG).show();
//            mFindItem.setText("");
//            barcode = "";
//        }
        if (mlistener != null) {
            Log.d("before passing result it", result.toString());
            mlistener.postResult(result);
        }
        else
        {
            Log.e("GetBarcode", "You have not assigned HTTPResponse delegate");
        }

    }
}
