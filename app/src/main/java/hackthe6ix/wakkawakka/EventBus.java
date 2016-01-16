package hackthe6ix.wakkawakka;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public class EventBus<G> {
    public static EventBus<LatLng> POSITION_UPDATE_EVENTBUS = new EventBus<>();

    private ArrayList<EventBusEventCallback<G>> listeners = new ArrayList<>();

    public void register(EventBusEventCallback<G> listener)
    {
        listeners.add(listener);
    }

    public void unregister(EventBusEventCallback<G> listener)
    {
        if (listeners.contains(listener))
        {
            listeners.remove(listener);
        }
    }

    public void broadcast(G param)
    {
        for (EventBusEventCallback list : listeners)
        {
            list.OnEvent(param);
        }
    }

}
