package businesslogic.event;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Event {
    private int id;
    private String name;
    private Date date;
    private Boolean refered = false;
    private static Map<Integer, Event> loadedEvents = FXCollections.observableHashMap();

    public Event() {}

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

    public int getId(){ return id;}

    public void setRefered() {
        this.refered = true;
    }


    public static ObservableList<Event> loadAllEvent() {
        String query = "SELECT * FROM Events WHERE id not in (SELECT event FROM SummarySheets)";
        ArrayList<Event> newEvents = new ArrayList<>();
        ArrayList<Event> oldEvents = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (loadedEvents.containsKey(id)) {
                    Event e = loadedEvents.get(id);
                    e.name = rs.getString("name");
                    e.date = rs.getDate("date");
                    e.refered = false;
                    oldEvents.add(e);
                } else {
                    Event e = new Event();
                    e.id = id;
                    e.name = rs.getString("name");
                    e.date = rs.getDate("date");
                    e.refered = false;
                    newEvents.add(e);
                }
            }
        });

        for (Event e: newEvents) {
            loadedEvents.put(e.id, e);
        }
        return FXCollections.observableArrayList(loadedEvents.values());
    }

    public static Event loadEventById(int id) {
        if (loadedEvents.containsKey(id)) return loadedEvents.get(id);

        Event e = new Event();
        String userQuery = "SELECT * FROM Events WHERE id='"+id+"'";
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                e.name = rs.getString("name");
                e.date = rs.getDate("date");
            }
        });
        return e;
    }

}
