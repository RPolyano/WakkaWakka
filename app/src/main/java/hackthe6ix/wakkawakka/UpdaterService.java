package hackthe6ix.wakkawakka;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.Timer;
import java.util.TimerTask;

public class UpdaterService extends Service implements Response.ErrorListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public final static String SERVER = "localhost:3000";
    public static final long NOTIFY_INTERVAL = 10 * 1000;

    private WakkaWebClient mWebClient;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    public UpdaterService() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID)
    {
        mWebClient = new WakkaWebClient(this, this, SERVER);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("VolleyError", error.getMessage());
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Error", connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Player.localplayer.accuracy = location.getAccuracy();
        EventBus.POSITION_UPDATE_EVENTBUS.broadcast(new LatLng(location.getLatitude(), location.getLongitude()));
        mWebClient.Update(new LatLng(Player.localplayer.latitude, Player.localplayer.longitude),
                Player.localplayer.accuracy, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Updated location "
                                + System.currentTimeMillis() + ":" + response, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
