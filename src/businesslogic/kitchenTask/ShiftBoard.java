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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShiftBoard {
    private static Map<Integer, Slot> list = FXCollections.observableHashMap();

    public static ObservableList<Slot> sortedList(){
        LinkedHashMap<Integer, Slot> Sorted = new LinkedHashMap<>();
        list.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> Sorted.put(x.getKey(), x.getValue()));


        return FXCollections.observableArrayList(Sorted.values());
    }

    public static ObservableList<Slot> loadAllShift(SummarySheet s) {
        int n=s.getId();
        list.clear();
        String query = "SELECT * FROM CookShifts;";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                if (!list.containsKey(id)) {

                    int shift_id = rs.getInt("shift_id");
                    int cook_id = rs.getInt("cook_id");

                    String query2 = "SELECT * FROM Shifts WHERE id='"+shift_id+"';";

                    PersistenceManager.executeQuery(query2, new ResultHandler() {
                        @Override
                        public void handle(ResultSet rs) throws SQLException {
                            int id4 = rs.getInt("id");
                            int start = rs.getInt("start");
                            int end = rs.getInt("end");
                            Date day = rs.getDate("day");

                            Shift s = new Shift(id4, start, end, day);

                            String query3 = "SELECT * FROM Users WHERE id='"+cook_id+"';";

                            PersistenceManager.executeQuery(query3, new ResultHandler() {
                                @Override
                                public void handle(ResultSet rs) throws SQLException {
                                    int id2 = rs.getInt("id");
                                    String username = rs.getString("username");
                                    String password = rs.getString("pass");
                                    String name = rs.getString("name");
                                    String surname = rs.getString("surname");
                                    String gender = rs.getString("gender");
                                    int age = rs.getInt("age");
                                    String email = rs.getString("email");
                                    Cook c = new Cook(id2,username,password,name,surname,gender,age,email);

                                    String query4 = "SELECT COUNT(*) FROM Tasks WHERE id in (SELECT task_id FROM TaskCookShifts WHERE shift_id='"+
                                            shift_id + "' and cook_id='"+cook_id+"') and summaryid="+n;
                                    PersistenceManager.executeQuery(query4, new ResultHandler() {
                                        @Override
                                        public void handle(ResultSet rs) throws SQLException {
                                            int count = rs.getInt("COUNT(*)");
                                            if (count > 0){

                                                String query5 = "SELECT * FROM Tasks WHERE id in (SELECT task_id FROM TaskCookShifts WHERE shift_id='"+
                                                        shift_id + "' and cook_id='"+cook_id+"') and summaryid="+n;
                                                PersistenceManager.executeQuery(query5, new ResultHandler() {
                                                    @Override
                                                    public void handle(ResultSet rs) throws SQLException {
                                                        int id3 = rs.getInt("id");
                                                        int timeEstimate = rs.getInt("timeEstimate");
                                                        int quantity = rs.getInt("quantity");
                                                        Boolean complete = rs.getBoolean("complete");
                                                        int idSummary = rs.getInt("summaryid");
                                                        int recipeid = rs.getInt("recipeid");
                                                        Recipe r = Recipe.loadRecipeById(recipeid);


                                                        Task t = new Task(id3,timeEstimate,quantity,complete,idSummary,r);
                                                        Slot slot = new Slot(id,s,c,t,false);
                                                        list.put(id,slot);

                                                    }
                                                });
                                            }
                                            else {
                                                Slot slot = new Slot(id,s,c,null,true);
                                                list.put(id,slot);
                                            }
                                        }
                                    });



                                }
                            });


                        }
                    });
                }

            }
        });

        return sortedList();
    }


    public static void saveChoose(Slot slot) {
        String querry = "INSERT INTO catering.TaskCookShifts(task_id, cook_id, shift_id) VALUES (?, ?, ?);";
        int[] result = PersistenceManager.executeBatchUpdate(querry, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, slot.getT().getId());
                ps.setInt(2, slot.getC().getId());
                ps.setInt(3,slot.getS().getId());
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                //fa niente
            }
        });
    }

    public static void dropChoose(Slot slot) {
        String querry = "DELETE FROM catering.TaskCookShifts WHERE cook_id='"+slot.getC().getId()+"' and '" +
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
