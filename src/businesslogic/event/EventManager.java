package businesslogic.event;

import javafx.collections.ObservableList;

public class EventManager {
    public ObservableList<Event> getEventInfo() {
        return Event.loadAllEventInfo();
    }
}
