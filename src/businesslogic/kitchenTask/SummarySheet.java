package businesslogic.kitchenTask;

import businesslogic.event.Event;
import businesslogic.user.User;

import java.util.ArrayList;

public class SummarySheet {
    private User creator;
    private Event event; // Otro cambio de nombre
    private ArrayList<Task> list;

    public SummarySheet(Event event,User user) {
        this.event = event;
        this.creator = user;
        this.list = new ArrayList<>();
    }


    public boolean isCreator(User user) {
        return user == this.creator;
    }

    @Override
    public String toString() {
        String result = "EVENTO: " + this.event.toString();
        result += "\n";
        result += "Creato per: " + this.creator.toString();
        result += "\n";
        if (this.list.size() > 0){
            result += "Compiti: \n";
            for (Task t: this.list){
                result += t.toString();
                result +="\n";
            }
        }
        return result;
    }
}
