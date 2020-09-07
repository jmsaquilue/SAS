package businesslogic.kitchenTask;

import businesslogic.UseCaseLogicException;
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
import java.util.Iterator;
import java.util.Map;

public class SummarySheet {
    private int id;
    private User creator;
    private Event event;
    private static Map<Integer, Task> list = FXCollections.observableHashMap();

    private static Map<Integer, SummarySheet> loadedSheets = FXCollections.observableHashMap();


    public SummarySheet(Event event,User user) {
        this.event = event;
        this.creator = user;

    }

    public SummarySheet(int id,Event event,User user) {
        this.id = id;
        this.event = event;
        this.creator = user;
    }

    public SummarySheet() {

    }

    public boolean isCreator(User user) {
        return user.getId() == this.creator.getId();
    }


    @Override
    public String toString() {
        String result = "EVENTO: " + this.event.toString();
        result += "\n";
        result += "Creato per: " + this.creator.toString();

        return result;
    }

    public void loadList() {
        list.entrySet().removeIf(t -> t.getKey() == 0);
        String query = "SELECT * FROM Tasks WHERE summaryid='"+id+"';";
        ArrayList<Task> newTask = new ArrayList<>();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int idT = rs.getInt("id");
                if (!list.containsKey(idT)) {
                    int timeEstimate = rs.getInt("timeEstimate");
                    int quantity = rs.getInt("quantity");
                    boolean complete = rs.getBoolean("complete");
                    int recipeId = rs.getInt("recipeid");
                    int pos = rs.getInt("pos");
                    Recipe r = businesslogic.recipe.Recipe.loadRecipeById(recipeId);
                    Task t = new Task(idT,timeEstimate,quantity,complete,id,r,pos);
                    newTask.add(t);
                }
            }
        });

        for (Task t: newTask){
            list.put(t.getId(), t);
        }

    }

    public Map<Integer, Task> getTasks(){
        loadList();
        return list;
    }

    public ArrayList<Task> getList(){
        loadList();
        return new ArrayList<Task>(list.values());
    }

    public void emptyList(int id){
        list.entrySet().removeIf(t -> t.getValue().getIdSummary() != id);
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



        for (SummarySheet s: newSheets) {
            loadedSheets.put(s.id, s);
        }
        return FXCollections.observableArrayList(loadedSheets.values());
    }

    public int getId(){
        return id;
    }

    public Event getEvent(){ return event;}

    public boolean exits(Recipe r) {
        Iterator<Map.Entry<Integer,Task>> it = list.entrySet().iterator();
        boolean exist = false;
        while (it.hasNext()) {
            Map.Entry<Integer,Task> e = it.next();
            if (e.getValue().getRecipeid() == r.getId()){
                exist = true;
                break;
            }
        }

        return exist;
    }

    public Task createTask(Recipe r)throws UseCaseLogicException, SummarySheetException{
        Task t = new Task(r, this.getId(),list.size());
        list.put(t.getId(),t);
        return t;
    }

    public void move(Recipe r, int pos) {
        Iterator<Map.Entry<Integer,Task>> it = list.entrySet().iterator();
        int oldPos = pos;
        Map.Entry<Integer,Task> e = null;
        while (it.hasNext()) {
            e = it.next();
            if (e.getValue().getRecipeid() == r.getId()){
                oldPos = e.getValue().getPos();
                break;

            }
        }
        Iterator<Map.Entry<Integer,Task>> it2 = list.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<Integer, Task> ee = it2.next();
            int currentPos = ee.getValue().getPos();
            if ((currentPos >= pos && currentPos < oldPos) || (currentPos <= pos && currentPos > oldPos)) {
                if (oldPos > pos)
                    ee.getValue().setPos(currentPos + 1);
                else
                    ee.getValue().setPos(currentPos - 1);
            }
        }
        e.getValue().setPos(pos);


    }

    public void deleteTask(Recipe r) {
        Iterator<Map.Entry<Integer,Task>> it = list.entrySet().iterator();
        boolean change = false;
        int oldPos = 0;
        while (it.hasNext()) {
            Map.Entry<Integer,Task> e = it.next();
            if (e.getValue().getRecipeid() == r.getId()){
                oldPos = e.getValue().getPos();
                break;
            }
        }

        Iterator<Map.Entry<Integer,Task>> it2 = list.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<Integer, Task> e = it2.next();
            int currentPos = e.getValue().getPos();
            if (currentPos > oldPos) {
                e.getValue().setPos(currentPos - 1);
            }
        }

        list.entrySet().removeIf(e -> e.getValue().getRecipeid()==r.getId());

    }

    public Boolean inList(Task t){
        Boolean b = false;
        for (Task tt: list.values()){
            if (t.getId() == tt.getId()){
                b = true;
            }
        }
        return b;
    }


}
