package hackthe6ix.wakkawakka;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public class Game extends Application {
    private static Game ourInstance;

    public static Game getInstance() {
        return ourInstance;
    }

    private static Context context;

    public static final int RELEVANCE_RANGE = 3000;

    public void onCreate() {
        super.onCreate();
        Game.context = getApplicationContext();
        ourInstance = this;
    }

    public static Context getAppContext() {
        return Game.context;
    }

    public ArrayList<Player> players;

    public Game() {
        players = new ArrayList<>();

    }

}
