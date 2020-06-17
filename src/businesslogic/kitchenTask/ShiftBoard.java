package businesslogic.kitchenTask;

import businesslogic.event.Event;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShiftBoard {
    private ArrayList<Shift> list;

    public ObservableList<Shift> loadAllShift() {

        //TODO: ver si cargas todos o solo los relativos a este ss
        // Ver que hacer con la lista que ya tienes, mandarla sin más o llamar siempre a la DB


        /*
        String query = "SELECT * FROM Shifts;";

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

        */
        return FXCollections.observableArrayList(list);
    }

}
