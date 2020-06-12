package businesslogic.kitchenTask;

import businesslogic.event.Event;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class SummarySheet {
    private int id;
    private User creator;
    private Event event; // Otro cambio de nombre
    private ArrayList<Task> list;
    private static Map<Integer, SummarySheet> loadedSheets = FXCollections.observableHashMap();


    public SummarySheet(Event event,User user) {
        this.event = event;
        this.creator = user;
        this.list = new ArrayList<>();
    }

    public SummarySheet() {
        this.list = new ArrayList<>();
    }

    public boolean isCreator(User user) {
        return user.getId() == this.creator.getId();
    }

    public Task createTask(Recipe r){
        Task t= new Task(r);
        list.add(t);
        return t;
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

    public static void saveNewSheet(SummarySheet s) {

        String querry = "INSERT INTO catering.SummarySheets (creator, event) VALUES (?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, s.creator.getId());
                ps.setInt(2, s.event.getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    s.id = rs.getInt(1);
                }
            }
        });
    }


    public static ObservableList<SummarySheet> loadAllSummarySheets() {
        String query = "SELECT * FROM SummarySheets;";
        ArrayList<SummarySheet> newSheets = new ArrayList<>();
        ArrayList<SummarySheet> oldSheets = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (loadedSheets.containsKey(id)) {
                    SummarySheet s = loadedSheets.get(id);
                    s.creator = User.loadUserById(rs.getInt("creator"));
                    s.event = Event.loadEventById(rs.getInt("event"));
                    oldSheets.add(s);
                } else {
                    SummarySheet s = new SummarySheet();
                    s.id = id;
                    s.creator = User.loadUserById(rs.getInt("creator"));
                    s.event = Event.loadEventById(rs.getInt("event"));
                    newSheets.add(s);
                }
            }
        });

        // TODO: es posible que haya que cargar las task


        for (SummarySheet s: newSheets) {
            loadedSheets.put(s.id, s);
        }
        return FXCollections.observableArrayList(loadedSheets.values());
    }

}
