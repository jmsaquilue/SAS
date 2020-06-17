package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Task {
    private int id;
    private int timeEstimate;
    private int quantity;
    private boolean complete;
    private int idSummary;
    private Recipe isPrepared; //No me gusta mucho el nombre
    private ArrayList<Shift> shifts; // He cambiado el nombre, habría que actualizar los gráficos

    public Task(Recipe r, int id){
        quantity=1;
        complete=false;
        isPrepared=r;
        idSummary=id;
    }

    public int getId(){
        return id;
    }

    public int getTimeEstimate() {
        return timeEstimate;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean getcomplete(){
        return complete;
    }
    public int getRecipeid(){
        return isPrepared.getId();
    }
    public int getIdSummary(){
        return idSummary;
    }

    public static void saveTask(Task t){
        String querry = "INSERT INTO catering.Tasks (timeEstimate, quantity, complete, recipeid, summaryid) VALUES (?, ?, ?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, t.getTimeEstimate());
                ps.setInt(2, t.getQuantity());
                ps.setBoolean(3,t.getcomplete());
                ps.setInt(4, t.getRecipeid());
                ps.setInt(5, t.getIdSummary());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    t.id = rs.getInt(1);
                }
            }
        });
    }
}
