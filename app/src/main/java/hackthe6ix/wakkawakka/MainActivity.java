package hackthe6ix.wakkawakka;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import hackthe6ix.wakkawakka.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Player.localplayer = new Player(true, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        EventBus.POSITION_UPDATE_EVENTBUS.register(Player.localplayer);
        EventBus.PLAYERS_UPDATED_EVENTBUS.register(Player.localplayer);

        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        final EditText txtName = (EditText) findViewById(R.id.txtName);

        if (sharedPref.contains("NAME"))
        {
            txtName.setText(sharedPref.getString("NAME", ""), TextView.BufferType.EDITABLE);
        }


        final Activity me = this;
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtName.getText().toString().isEmpty())
                {
                    Toast.makeText(me, "Please enter a nickname!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Player.localplayer.name = txtName.getText().toString();
                sharedPref.edit().putString("NAME", Player.localplayer.name).commit();
                WakkaWebClient.getInstance().Create(
                        Player.localplayer.name,
                        Player.localplayer.devid,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Player.localplayer.Update(response);
                                LaunchMap();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse == null)
                                {
                                    Toast.makeText(me, "A network error occurred. Check your connection and try again.", Toast.LENGTH_LONG).show();
                                }
                                else if (error.networkResponse.statusCode == 400)
                                {
                                    Toast.makeText(me, "That name is already taken. Try another!", Toast.LENGTH_LONG).show();
                                }
                                //Log.e("VolleyError", error.getMessage());
                            }
                        });
            }
        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    void LaunchMap()
    {
        Intent mapLaunch = new Intent(this, MapsActivity.class);
        startActivity(mapLaunch);
    }

}
