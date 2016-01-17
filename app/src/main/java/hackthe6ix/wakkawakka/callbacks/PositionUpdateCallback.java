package hackthe6ix.wakkawakka.callbacks;

import com.google.android.gms.maps.model.LatLng;

import hackthe6ix.wakkawakka.eventbus.EventBusEvent;

public interface PositionUpdateCallback {

    @EventBusEvent
    void OnPositionUpdate(LatLng pos);
}
