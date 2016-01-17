package hackthe6ix.wakkawakka;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import hackthe6ix.wakkawakka.callbacks.NotificationEvent;
import hackthe6ix.wakkawakka.callbacks.PlayerCreatedCallback;
import hackthe6ix.wakkawakka.callbacks.PositionUpdateCallback;
import hackthe6ix.wakkawakka.eventbus.EventBus;
import hackthe6ix.wakkawakka.services.LocationUpdaterService;
import hackthe6ix.wakkawakka.services.PlayerUpdateService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        PositionUpdateCallback, PlayerCreatedCallback {

    private GoogleMap mMap;
    private boolean mapNeedsToRefocus;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            StartMap();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    StartMap();
                } else {

                    finish();
                }
                return;
            }

        }
    }

    void StartMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapNeedsToRefocus = true;


        EventBus.POSITION_UPDATE_EVENTBUS.register(this);
        EventBus.PLAYER_CREATE_EVENTBUS.register(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent locationUpdater = new Intent(this, LocationUpdaterService.class);
        startService(locationUpdater);

        Intent intent = new Intent(this, PlayerUpdateService.class);
        startService(intent);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    protected void onStop() {
        EventBus.POSITION_UPDATE_EVENTBUS.unregister(this);
        EventBus.PLAYER_CREATE_EVENTBUS.unregister(this);
        super.onStop();
    }

    @Override
    public void OnPositionUpdate(LatLng ev) {
        if (mapNeedsToRefocus) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ev, 17f));
            mapNeedsToRefocus = false;
        }
    }

    public void MaintainPlayerMarkers(Player plr)
    {
        if (plr.marker == null)
        {
            Marker mark = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("You"));
            mark.setVisible(true);
            plr.marker = mark;
        }

        if (plr.accuracyCircle == null) {
            CircleOptions opts = new CircleOptions();

            opts.center(new LatLng(plr.latitude, plr.longitude));
            opts.fillColor(Color.argb(plr.local ? 100 : 25, plr.local ? 255 : 0, 0, plr.local ? 0 : 255));
            opts.zIndex(-1f);
            opts.strokeColor(Color.argb(200, 255, 0, 0));
            opts.strokeWidth(1.5f);

            plr.accuracyCircle = mMap.addCircle(opts);
        }


    }

    @Override
    public void OnPlayerCreated(Player plr) {
        if (mMap != null)
        {
            MaintainPlayerMarkers(plr);
        }

    }

}
