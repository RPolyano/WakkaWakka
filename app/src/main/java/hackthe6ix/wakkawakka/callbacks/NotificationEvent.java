package hackthe6ix.wakkawakka.callbacks;

import hackthe6ix.wakkawakka.eventbus.EventBusEvent;

/**
 * Created by uba19_000 on 1/17/2016.
 */
public interface NotificationEvent {
    public class NotificationInfo
    {
        public String text;
        public int iconType;

        public NotificationInfo(int iconType, String text)
        {
            this.iconType = iconType;
            this.text = text;
        }
    }

    @EventBusEvent
    void OnNotify(NotificationInfo info);
}
