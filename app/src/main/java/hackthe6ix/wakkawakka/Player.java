package hackthe6ix.wakkawakka;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import hackthe6ix.wakkawakka.callbacks.NotificationEvent;
import hackthe6ix.wakkawakka.callbacks.PlayerUpdateRecievedCallback;
import hackthe6ix.wakkawakka.callbacks.PositionUpdateCallback;
import hackthe6ix.wakkawakka.eventbus.EventBus;

/**
 * Created by uba19_000 on 1/15/2016.
 */
public class Player implements PositionUpdateCallback, PlayerUpdateRecievedCallback {
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
    public final boolean local;
    private int presenceAck;

    public Player(boolean local, String devid) {
        this.local = local;
        this.devid = devid;
        prevType = -1;
        presenceAck = 0;
    }

    @Override
    public void OnPositionUpdate(LatLng ev) {
        latitude = ev.latitude;
        longitude = ev.longitude;
        //Notify(PlayerType.getDrawableID(0), "Hello!");
    }


    public void Update(JSONObject jsonObject) {
        try {
            name = jsonObject.getString("username");

            if (!local) {
                JSONObject location = jsonObject.getJSONObject("location");
                latitude = Double.parseDouble(location.getString("y"));
                longitude = Double.parseDouble(location.getString("x"));
                accuracy = Double.parseDouble(location.getString("Acc"));

            }

            score = jsonObject.getInt("points");
            invulnerable = jsonObject.getInt("invulnerable");
            cooldown = jsonObject.getInt("cooldown");
            type = Integer.parseInt(jsonObject.getString("type"));

            if (type != prevType && marker != null && accuracyCircle != null) {
                prevType = type;
                UpdateMarker();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void UpdateMarker() {
        marker.setTitle(PlayerType.getTypeString(type) + " " + (local ? "You" : name));
        int drawableID = PlayerType.getDrawableID(type);
        Bitmap bmp = BitmapFactory.decodeResource(Game.getAppContext().getResources(), drawableID);
        double aspect = bmp.getWidth() / (double) bmp.getHeight();
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bmp, 100, (int) (100 / aspect), false)));
        marker.setVisible(true);
        marker.setPosition(new LatLng(latitude, longitude));
        marker.setSnippet("Accurate to " + (int) accuracy + " meters");
        accuracyCircle.setCenter(new LatLng(latitude, longitude));
        accuracyCircle.setRadius(Player.localplayer.accuracy);
    }


    @Override
    public void OnPlayersUpdated(Integer num) {
        boolean meInvoln = System.currentTimeMillis() - invulnerable < Game.INVULN_TIME;
        if (System.currentTimeMillis() - cooldown < Game.COOLDOWN_TIME) {
            return;
        }

        //Interactions check
        for (final Player plr : Game.getInstance().players.values()) {
            if (System.currentTimeMillis() - plr.cooldown < Game.COOLDOWN_TIME) {
                continue;
            }
            boolean targetInvuln = System.currentTimeMillis() - plr.invulnerable < Game.INVULN_TIME;
            double dist = LatLonDist(plr.latitude, latitude, plr.longitude, longitude);
            if (dist < Game.INTERACTION_RANGE && PlayerType.CanInteract(type, meInvoln, plr.type, targetInvuln)) {
                WakkaWebClient.getInstance().Interact(plr.devid, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Notify Interact success

                        EventBus.NOTIFICATION_EVENTBUS.broadcast(
                                new NotificationEvent.NotificationInfo(type,
                                        "You ate " + plr.name + " the " +
                                                PlayerType.getTypeString(plr.type) + "!"));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Could not interact with " + plr.devid);
                        Toast.makeText(Game.getAppContext(), "A network error occured - could not interact.", Toast.LENGTH_SHORT);
                    }
                });
            } else if (dist < Game.THREAT_RANGE && PlayerType.CanInteract(plr.type, targetInvuln, type, meInvoln) && plr.presenceAck != 1) {
                //Notify of threat
                //Notify(plr.type, plr.name + " the " + PlayerType.getTypeString(plr.type) + " is nearby, and can eat you!");
                plr.presenceAck = 1;
                EventBus.NOTIFICATION_EVENTBUS.broadcast(new NotificationEvent.NotificationInfo(plr.type,
                        plr.name + " the " + PlayerType.getTypeString(plr.type) + " is nearby, and can eat you!"));
            }
            else
            {
                plr.presenceAck = 0;
            }
        }
    }

    //find distance between coords
    public static double LatLonDist(double lat1, double lat2, double lon1, double lon2) {
        double R = 6371000; // metres
        double Lat1 = lat1 * Math.PI / 180;
        double Lat2 = lat2 * Math.PI / 180;
        double deltalat = (lat2 - lat1) * Math.PI / 180;
        double deltaAlpha = (lon2 - lon1) * Math.PI / 180;

        double a = Math.sin(deltalat / 2) * Math.sin(deltalat / 2) +
                Math.cos(Lat1) * Math.cos(Lat2) *
                        Math.sin(deltaAlpha / 2) * Math.sin(deltaAlpha / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c;
        return d;
    }
}
