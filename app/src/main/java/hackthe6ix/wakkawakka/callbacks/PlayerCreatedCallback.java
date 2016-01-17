package hackthe6ix.wakkawakka.callbacks;

import hackthe6ix.wakkawakka.Player;
import hackthe6ix.wakkawakka.eventbus.EventBusEvent;

/**
 * Created by uba19_000 on 1/17/2016.
 */
public interface PlayerCreatedCallback {

    @EventBusEvent
    void OnPlayerCreated(Player plr);
}
