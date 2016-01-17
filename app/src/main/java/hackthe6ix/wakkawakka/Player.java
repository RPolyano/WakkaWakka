package hackthe6ix.wakkawakka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import hackthe6ix.wakkawakka.callbacks.PlayerUpdateCallback;
import hackthe6ix.wakkawakka.callbacks.PositionUpdateCallback;

/**
 * Created by uba19_000 on 1/15/2016.
 */
public class Player implements PositionUpdateCallback, PlayerUpdateCallback {
    public static Player localplayer;
    public double longitude;
    public double latitude;
    public double accuracy;
    public int type;
    public int score;
    public String name;
    public long lastUpdateTime;

    public Marker marker;
    public Circle accuracyCircle;

    private int prevType;
    private final boolean local;

    public Player(boolean local)
    {
        this.local = local;
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


    @Override
    public void OnPlayerInfoUpdated(Player info) {
        name = info.name;
        if (!local)
        {
            latitude = info.latitude;
            longitude = info.longitude;
            accuracy = info.accuracy;
        }

        type = info.type;
        if (type != prevType)
        {
            Bitmap bmp = BitmapFactory.decodeResource(Game.getAppContext().getResources(), R.drawable.wakkman);
            double aspect = bmp.getWidth()/(double)bmp.getHeight();
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bmp, 100, (int) (100 / aspect), false)));
            prevType = type;
        }
        score = info.score;
    }


}
