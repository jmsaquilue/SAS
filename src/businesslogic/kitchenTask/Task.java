package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

public class Task {
    private int id;
    private int timeEstimate;
    private int quantity;
    private boolean complete;
    private int idSummary;
    private Recipe isPrepared;
    private int pos;





    public Task(){

    }




    @Override
    public String toString() {
        String r= "";
        if(isPrepared != null){
            r = r + " Ricetta: " + isPrepared.toString();
        }
        return r + ". \n";
    }

    public String show() {
        String r= "";
        r=" Time Estimate=" + timeEstimate +
                ", quantity=" + quantity;
        if(complete){
            r= r + " Completado ";
        }else {
            r= r + " Non completado ";
        }
        if(isPrepared != null){
            r = r + " Receta: " + isPrepared.toString();
        }
        return r + ". \n";
    }

    public Task(Recipe r, int id, int pos){
        this.id = 0;
        this.pos = pos;
        quantity=1;
        complete=false;
        isPrepared=r;
        idSummary=id;
    }

    public Task(int id, int timeEstimate, int quantity, boolean complete, int idSummary, Recipe recipe, int pos) {
        this.id = id;
        this.timeEstimate = timeEstimate;
        this.quantity = quantity;
        this.complete = complete;
        this.idSummary = idSummary;
        this.isPrepared = recipe;
        this.pos = pos;
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
    public int getPos(){return pos;}

    public void setQuantity(int q){quantity=q;}
    public void setTimeEstimate(int t){timeEstimate=t;}

    public static void saveTask(Task t){
        String querry = "INSERT INTO catering.Tasks (timeEstimate, quantity, complete, recipeid, summaryid, pos) VALUES (?, ?, ?, ?, ?,?);";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, t.getTimeEstimate());
                ps.setInt(2, t.getQuantity());
                ps.setBoolean(3,t.getcomplete());
                ps.setInt(4, t.getRecipeid());
                ps.setInt(5, t.getIdSummary());
                ps.setInt(6,t.getPos());
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

    public static void deleteRecipe(Recipe r, SummarySheet sheet){
        String querry = "DELETE FROM catering.Tasks WHERE recipeid='"+r.getId()+"' and summaryid='" +
                +sheet.getId()+ "';";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                //fa niente
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                //fa niente
            }
        });
        sortTasks(sheet);

    }



    public static void saveQuantity(Task t) {
        String querry = "UPDATE Tasks SET quantity="+t.getQuantity()+" WHERE id=" +t.getId()+";";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                //fa niente
            }
        });
    }

    public static void saveTime(Task t) {
        String querry = "UPDATE Tasks SET timeEstimate="+t.getTimeEstimate()+" WHERE id=" +t.getId()+";";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                //fa niente
            }
        });
    }


    public void setPos(int pos) {
        this.pos = pos;
    }

    public static void sortTasks(SummarySheet sheet) {
        Map<Integer,Task> list = sheet.getTasks();
        Iterator<Map.Entry<Integer,Task>> it = list.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer,Task> e = it.next();
            Task t = e.getValue();
            String querry = "UPDATE Tasks SET pos="+t.getPos()+" WHERE id=" +t.getId()+";";
            int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
                @Override
                public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                }

                @Override
                public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                    //fa niente
                }
            });
        }

    }
}
