package hackthe6ix.wakkawakka.eventbus;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import hackthe6ix.wakkawakka.Player;
import hackthe6ix.wakkawakka.callbacks.NotificationEvent;
import hackthe6ix.wakkawakka.callbacks.PlayerCreatedCallback;
import hackthe6ix.wakkawakka.callbacks.PlayerUpdateRecievedCallback;
import hackthe6ix.wakkawakka.callbacks.PositionUpdateCallback;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public class EventBus<T, G> {
    final Class<T> typeParameterClass;

    public static EventBus<PositionUpdateCallback, LatLng> POSITION_UPDATE_EVENTBUS = new EventBus<>(PositionUpdateCallback.class);
    public static EventBus<PlayerCreatedCallback, Player> PLAYER_CREATE_EVENTBUS = new EventBus<>(PlayerCreatedCallback.class);
    public static EventBus<PlayerUpdateRecievedCallback, Integer> PLAYERS_UPDATED_EVENTBUS = new EventBus<>(PlayerUpdateRecievedCallback.class);
    public static EventBus<NotificationEvent, NotificationEvent.NotificationInfo> NOTIFICATION_EVENTBUS = new EventBus<>(NotificationEvent.class);

    private ArrayList<T> listeners = new ArrayList<>();

    private Method eventMethod;

    public EventBus(Class<T> type)
    {
        typeParameterClass = type;
        for(Method meth : typeParameterClass.getMethods())
        {
            if (meth.isAnnotationPresent(EventBusEvent.class))
            {
                eventMethod = meth;
                break;
            }
        }

    }

    public void register(T listener) {
        listeners.add(listener);
    }

    public void unregister(T listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void broadcast(G param) {
        for (T list : listeners) {
            try {
                eventMethod.invoke(list, param);
            } catch (IllegalAccessException e) {
                Log.e("EventBus", "Access Error: ", e);
            } catch (InvocationTargetException e) {
                Log.e("EventBus", "Invocation Error: ", e);
            }
        }
    }

}
