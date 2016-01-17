package hackthe6ix.wakkawakka;

import android.content.Context;
import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public class WakkaWebClient {
    private static final String FMT_CREATE_NAME_DEVID = "http://%s/player/create?name=%s&dev_id=%s";
    private static final String FMT_LIST_AROUND_X_Y_RADIUS = "http://%s/players/list/around?x=%f&y=%f&radius=%f";
    private static final String FMT_UPDATE_X_Y_ACCURACY_NAME = "http://%s/players/update?x=%f&y=%f&accuracy=%f&name=%4s";
    private static final String TAG_UPDATE_REQUEST = "UPDATE_REQ";
    private static  final String HOST = "localhost:3000";

    RequestQueue queue;
    String devid;

    private static WakkaWebClient instance = new WakkaWebClient(Game.getAppContext());

    public static WakkaWebClient getInstance()
    {
        return instance;
    }

    private WakkaWebClient(Context ctx) {
        queue = Volley.newRequestQueue(ctx);
        devid = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void Create(String name, Response.Listener<JSONObject> resp, Response.ErrorListener errorListener) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                String.format(FMT_CREATE_NAME_DEVID, HOST, name, devid), null, resp, errorListener);

        queue.add(req);
    }

    public void ListAround(LatLng pos, double radius, Response.Listener<JSONArray> resp, Response.ErrorListener errorListener) {
        JsonArrayRequest req = new JsonArrayRequest(
                String.format(FMT_LIST_AROUND_X_Y_RADIUS, HOST, pos.longitude, pos.latitude, radius),
                resp, errorListener);
        req.setShouldCache(false);
        queue.add(req);
    }

    public void Update(LatLng pos, double accuracy, Response.Listener<String> resp, Response.ErrorListener errorListener) {
        queue.cancelAll(TAG_UPDATE_REQUEST);
        StringRequest req = new StringRequest(Request.Method.POST,
                String.format(FMT_UPDATE_X_Y_ACCURACY_NAME, HOST, pos.longitude, pos.latitude, accuracy, devid),
                resp, errorListener
        );

        req.setTag(TAG_UPDATE_REQUEST);
        req.setShouldCache(false);
        queue.add(req);
    }
}
