package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;
import businesslogic.user.Cook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ShiftBoard {
    private static ArrayList<Slot> list = new ArrayList<Slot>() ;

    public static ObservableList<Slot> sortList(){
        Collections.sort(list);
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList<Slot> loadAllShift(SummarySheet s) {
        int n=s.getId();
        list.clear();
        String query = "SELECT * FROM Shifts s WHERE (s.id IN (SELECT shift_id FROM CookShifts)) and ((SELECT count(*) FROM  CookShifts WHERE shift_id=s.id) > (SELECT count(*) FROM  TaskCookShifts WHERE shift_id=s.id));";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id4 = rs.getInt("id");
                int start = rs.getInt("start");
                int end = rs.getInt("end");
                Date day = rs.getDate("day");

                ArrayList<Cook> availables = new ArrayList<>();

                String query33 = "SELECT * FROM Users u WHERE (u.id IN (SELECT cook_id FROM CookShifts WHERE shift_id='" + id4 + "'))" +
                        "and (u.id NOT IN (SELECT cook_id FROM TaskCookShifts WHERE shift_id='" + id4 + "'))";
                PersistenceManager.executeQuery(query33, new ResultHandler() {
                    public void handle(ResultSet rs) throws SQLException {
                        int id = rs.getInt("id");
                        String username = rs.getString("username");
                        String password = rs.getString("pass");
                        String name = rs.getString("name");
                        String surname = rs.getString("surname");
                        String gender = rs.getString("gender");
                        int age = rs.getInt("age");
                        String email = rs.getString("email");
                        Cook c = new Cook(id, username, password, name, surname, gender, age, email);
                        availables.add(c);
                    }
                });

                Shift s = new Shift(id4, start, end, day, availables);
                Slot slot = new Slot(s,null,null,true);
                list.add(slot);
            }
        });



        query = "SELECT * FROM TaskCookShifts;";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int cook_id = rs.getInt("cook_id");
                int task_id = rs.getInt("task_id");
                int shift_id = rs.getInt("shift_id");
                
                String querry2 = "SELECT * FROM Shifts WHERE id='"+shift_id+"';";
                PersistenceManager.executeQuery(querry2, new ResultHandler() {
                    @Override
                    public void handle(ResultSet rs) throws SQLException {
                        int id4 = rs.getInt("id");
                        int start = rs.getInt("start");
                        int end = rs.getInt("end");
                        Date day = rs.getDate("day");

                        Shift s = new Shift(id4, start, end, day, null);
                        String query3 = "SELECT * FROM Tasks WHERE id='"+task_id+"' and summaryid="+n;
                        PersistenceManager.executeQuery(query3, new ResultHandler() {
                            @Override
                            public void handle(ResultSet rs) throws SQLException {
                                int id3 = rs.getInt("id");
                                int timeEstimate = rs.getInt("timeEstimate");
                                int quantity = rs.getInt("quantity");
                                Boolean complete = rs.getBoolean("complete");
                                int idSummary = rs.getInt("summaryid");
                                int recipeid = rs.getInt("recipeid");
                                int pos = rs.getInt("pos");
                                Recipe r = Recipe.loadRecipeById(recipeid);
                                Task t = new Task(id3, timeEstimate, quantity, complete, idSummary, r, pos);

                                if (cook_id != 0) {
                                    String query33 = "SELECT * FROM Users WHERE id='" + cook_id + "';";
                                    PersistenceManager.executeQuery(query33, new ResultHandler() {
                                        public void handle(ResultSet rs) throws SQLException {
                                            int id = rs.getInt("id");
                                            String username = rs.getString("username");
                                            String password = rs.getString("pass");
                                            String name = rs.getString("name");
                                            String surname = rs.getString("surname");
                                            String gender = rs.getString("gender");
                                            int age = rs.getInt("age");
                                            String email = rs.getString("email");
                                            Cook c = new Cook(id, username, password, name, surname, gender, age, email);
                                            System.out.println(c);
                                            Slot slot = new Slot(s, c, t, false);
                                            list.add(slot);
                                        }
                                    });
                                } else {

                                    Slot slot = new Slot(s, null, t, false);
                                    list.add(slot);
                                }
                            }
                        });

                    }
                });

            }
        });

        return sortList();
    }


    public static void saveChoose(Slot slot) {
        String querry = "INSERT INTO catering.TaskCookShifts(task_id, cook_id, shift_id) VALUES (?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, slot.getT().getId());
                if (slot.getC() != null)
                    ps.setInt(2, slot.getC().getId());
                else {
                    ps.setNull(2,java.sql.Types.INTEGER);
                }
                ps.setInt(3,slot.getS().getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                //fa niente
            }
        });
    }

    public static void dropChoose(Slot slot,Task t) {
        String querry = "DELETE FROM catering.TaskCookShifts WHERE task_id='"+t.getId()+"' and shift_id='" +
                +slot.getS().getId()+ "';";
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
    }
}
