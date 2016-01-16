package hackthe6ix.wakkawakka;

/**
 * Created by uba19_000 on 1/16/2016.
 */
public interface EventBusEventCallback<G> {
    void OnEvent(G ev);
}
