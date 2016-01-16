package hackthe6ix.wakkawakka;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by uba19_000 on 1/15/2016.
 */
public class Player implements EventBusEventCallback<LatLng> {
    public static Player localplayer;
    public double longitude;
    public double latitude;
    public double accuracy;
    public int type;
    public int score;
    public String name;
    public Marker marker;

    @Override
    public void OnEvent(LatLng ev) {
        latitude = ev.latitude;
        longitude = ev.longitude;
        marker.setVisible(true);
        marker.setPosition(ev);
        marker.setSnippet("Accurate to " + (int) accuracy + " meters\n" + "Lat: " + latitude + " Lon: " + longitude);
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.));
    }
}
