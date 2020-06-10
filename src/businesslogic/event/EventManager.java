package businesslogic.event;

import java.util.ArrayList;

public class EventManager {
    public ArrayList<Event> getAvailableEvents() {
        return Event.loadAllEvent();
    }

}
