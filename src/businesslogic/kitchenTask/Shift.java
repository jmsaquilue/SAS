package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;
import businesslogic.user.Cook;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Queue;

public class Shift {
    private int id;
    private int start;
    private int end;
    private Date day;
    private ArrayList<Cook> availables;
    public Shift(){

    }
    public Shift(int id,int start,int end,Date day,ArrayList<Cook> availables){
        this.id=id;
        this.start=start;
        this.end=end;
        this.day=day;
        this.availables = availables;
    }

    public String toString(){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(day);
        return "Inizio: " + start + ", Fine: "+ end + ", Giorno: " + date+ ".\n";
    }
    public Integer getId() {
        return id;
    }
    public Date getDay() {return day;}
    public int getStart() {return start;}
    public ArrayList<Cook>  getCooks() {return availables;}


    public void removeCook(Cook c) {
        availables.removeIf(cc -> cc.getId() == c.getId());
    }

    public void addCook(Cook c) {
        availables.add(c);
    }

    public Map<Integer,Task> getAvailableTask(Map<Integer,Task> tasks){

        String query = "SELECT task_id FROM  TaskCookShifts WHERE shift_id='"+id+"';";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int idT = rs.getInt("task_id");
                if (tasks.containsKey(idT)) {
                    tasks.remove(idT);
                }
            }
        });
        return tasks;
    }

    public Boolean isFull() {
        final int[] full = new int[1];
        String query = "SELECT a - b  FROM (SELECT COUNT(*) a FROM  CookShifts WHERE shift_id='"+id+"') aa," +
            "(SELECT COUNT(*) b FROM  TaskCookShifts WHERE shift_id='"+id+"') bb;";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                full[0] = rs.getInt(1);
            }
        });

        System.out.println(full[0]);
        return full[0] == 0;

    }
}
