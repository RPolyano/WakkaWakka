package hackthe6ix.wakkawakka;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public class Game extends Application {
    private static Game ourInstance;

    public static Game getInstance() {
        return ourInstance;
    }

    private static Context context;

    public static final double INVULN_TIME = 3.6 * Math.pow(10, 6);
    public static final double COOLDOWN_TIME = 1.8 * Math.pow(10, 6);
    public static final int INTERACTION_RANGE = 10;
    public static final int THREAT_RANGE = 10;
    public static final int RELEVANCE_RANGE = 3000;
    public static final int LOCATION_UPDATE_RATE = 20000;
    public static final int PLAYER_UPDATE_RATE = 20000;

    public void onCreate() {
        super.onCreate();
        Game.context = getApplicationContext();
        ourInstance = this;
    }

    public static Context getAppContext() {
        return Game.context;
    }

    public Map<String, Player> players;

    public boolean isPlayerRegistered(String devid)
    {
        return players.containsKey(devid);
    }

    public Game() {
        players = new HashMap<>();
    }



}
