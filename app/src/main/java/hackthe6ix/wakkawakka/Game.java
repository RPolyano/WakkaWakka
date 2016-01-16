package hackthe6ix.wakkawakka;

import java.util.ArrayList;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public class Game {
    private static Game ourInstance = new Game();

    public static Game getInstance() {
        return ourInstance;
    }

    public ArrayList<Player> players;

    private Game() {
        players = new ArrayList<>();
    }

}
