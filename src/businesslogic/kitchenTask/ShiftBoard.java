package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;
import businesslogic.user.Cook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class ShiftBoard {
    private static Map<Integer, Slot> list = FXCollections.observableHashMap();

    /*
    public static ObservableList<Slot> loadAllShift() {

        //TODO: ver si cargas todos o solo los relativos a este ss
        // Ver que hacer con la lista que ya tienes, mandarla sin más o llamar siempre a la DB

        String query = "SELECT * FROM Shifts;";

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                int start = rs.getInt("start");
                int end = rs.getInt("end");
                Date day = rs.getDate("day");

                Shift s = new Shift(id,start,end,day);

                String query2 = "SELECT * FROM Users WHERE id in (SELECT cook_id FROM CookShifts WHERE shift_id='"+
                        id + "');";


                PersistenceManager.executeQuery(query2, new ResultHandler() {
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

                        String query3 = "SELECT COUNT(*) FROM Tasks WHERE id in (SELECT task_id FROM TaskCookShifts WHERE shift_id='"+
                                id + "' and cook_id='"+id2+"');";
                        PersistenceManager.executeQuery(query3, new ResultHandler() {
                            @Override
                            public void handle(ResultSet rs) throws SQLException {
                                int count = rs.getInt("COUNT(*)");
                                if (count > 0){
                                    String query4 = "SELECT * FROM Tasks WHERE id in (SELECT task_id FROM TaskCookShifts WHERE shift_id='"+
                                            id + "' and cook_id='"+id2+"');";
                                    PersistenceManager.executeQuery(query4, new ResultHandler() {
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
                                            Slot slot = Slot.loadSlot(t,c,s);
                                            if (!list.containsKey(slot.getId())) list.put(slot.getId(), slot);
                                        }
                                    });
                                }
                                else {
                                    Slot slot = Slot.loadSlot(null,c,s);
                                    if (!list.containsKey(slot.getId())) list.put(slot.getId(), slot);
                                }
                            }
                        });
                    }
                });
            }
        });

        return FXCollections.observableArrayList(list.values());
    }


     */

    public static ObservableList<Slot> loadAllShift() {
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
                                            shift_id + "' and cook_id='"+cook_id+"');";
                                    PersistenceManager.executeQuery(query4, new ResultHandler() {
                                        @Override
                                        public void handle(ResultSet rs) throws SQLException {
                                            int count = rs.getInt("COUNT(*)");
                                            if (count > 0){

                                                String query5 = "SELECT * FROM Tasks WHERE id in (SELECT task_id FROM TaskCookShifts WHERE shift_id='"+
                                                        shift_id + "' and cook_id='"+cook_id+"');";
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


        return FXCollections.observableArrayList(list.values());
    }


    public static void saveChoose(Task t, Slot slot) {
        //TODO.
    }

}
