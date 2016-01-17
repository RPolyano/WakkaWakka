package hackthe6ix.wakkawakka.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import hackthe6ix.wakkawakka.Game;
import hackthe6ix.wakkawakka.Player;
import hackthe6ix.wakkawakka.WakkaWebClient;

public class PlayerUpdateService extends Service {

    public PlayerUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private final static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("VolleyError", error.getMessage());
        }
    };
    private final static Handler updateOpRunner = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            WakkaWebClient client = WakkaWebClient.getInstance();
            client.ListAround(new LatLng(Player.localplayer.latitude, Player.localplayer.longitude),
                    Game.RELEVANCE_RANGE, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                        }
                    }, errorListener);
        }
    };

}
