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

    public static boolean CanInteract(int actorType, boolean actorInvoln, int targetType, boolean targetInvoln) {
        switch (actorType)
        {
            case WAKKMAN:
                return targetType == FOOD || targetType == SUPERFOOD || (actorInvoln && targetType == GHOST);
            case GHOST:
                return targetType == WAKKMAN && !targetInvoln;
            case FOOD:
                return false;
            case SUPERFOOD:
                return false;
            case INVALID:
                return false;
        }
        return false;
    }

    public static int getDrawableID(int type) {
        switch (type) {
            case PlayerType.WAKKMAN:
                return R.drawable.wakkman;
            case PlayerType.FOOD:
                return R.drawable.food;
            case PlayerType.GHOST:
                return R.drawable.ghost;
            case PlayerType.SUPERFOOD:
                return R.drawable.superfood;
            case PlayerType.INVALID:
                return 0;
        }
        return 0;
    }
}
