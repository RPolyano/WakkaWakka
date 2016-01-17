package hackthe6ix.wakkawakka;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public final class PlayerType {
    public static final int INVALID = -1;
    public static final int WAKKMAN = 0;
    public static final int GHOST = 1;
    public static final int FOOD = 2;
    public static final int SUPERFOOD = 3;


    public static String getTypeString(int type) {
        switch (type)
        {
            case WAKKMAN:
                return "WakkMan";
            case GHOST:
                return "Ghost";
            case FOOD:
                return "Food";
            case SUPERFOOD:
                return "Super Food";
            case INVALID:
                return "Invalid";
        }
        return "Unknown";
    }
}
