package businesslogic.event;

import java.util.Date;

public class Event {
    private String name;
    private Date date;
    private Boolean refered = false;

    public Event(String name,Date date){
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return this.name + ", data: " + date;
    }

    public boolean isRefered() {
        return refered;
    }

    public void setRefered() {
        this.refered = true;
    }
}
