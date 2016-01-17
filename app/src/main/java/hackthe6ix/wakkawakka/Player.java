package hackthe6ix.wakkawakka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import hackthe6ix.wakkawakka.callbacks.PositionUpdateCallback;

/**
 * Created by uba19_000 on 1/15/2016.
 */
public class Player implements PositionUpdateCallback {
    public static Player localplayer;
    public double longitude;
    public double latitude;
    public double accuracy;
    public int type;
    public int score;
    public String name;
    public long lastUpdateTime;
    public String devid;
    public long invulnerable;
    public long cooldown;

    public Marker marker;
    public Circle accuracyCircle;

    private int prevType;
    private final boolean local;

    public Player(boolean local, String devid)
    {
        this.local = local;
        this.devid = devid;
        prevType = -1;
    }

    @Override
    public void OnPositionUpdate(LatLng ev) {
        latitude = ev.latitude;
        longitude = ev.longitude;
        marker.setVisible(true);
        marker.setPosition(ev);
        marker.setSnippet("Accurate to " + (int) accuracy + " meters");
    }


    public void Update(JSONObject jsonObject) {
        try {
            name = jsonObject.getString("username");
            if (!local)
            {
                JSONObject location = jsonObject.getJSONObject("location");
                latitude = Double.parseDouble(location.getString("y"));
                longitude = Double.parseDouble(location.getString("x"));
                accuracy = Double.parseDouble(location.getString("Acc"));
            }
            type = Integer.parseInt(jsonObject.getString("type"));
            score = jsonObject.getInt("points");
            invulnerable = jsonObject.getInt("invulnerable");
            cooldown = jsonObject.getInt("cooldown");

            if (type != prevType)
            {
                Bitmap bmp = BitmapFactory.decodeResource(Game.getAppContext().getResources(), R.drawable.wakkman);
                double aspect = bmp.getWidth()/(double)bmp.getHeight();
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bmp, 100, (int) (100 / aspect), false)));
                prevType = type;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
