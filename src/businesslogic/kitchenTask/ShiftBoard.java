package businesslogic.kitchenTask;

import businesslogic.event.Event;
import businesslogic.recipe.Recipe;
import businesslogic.user.Cook;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ShiftBoard {
    private static Map<String, Foo> list = FXCollections.observableHashMap();

    public static ObservableList<Foo> loadAllShift() {

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



                    // TODO: es posible que tengas que meter en la misma tabla turnos cocineros y tareas
                    // TODO: seguramente, primero tendras que cargar los cocineros y si enable es false cargar lo que está haciendo.
                    // TODO: hay un problema estructural

                    // TODO: pensar si usar  User u = User.loadUserById(id2);
                    // Pros: mas simple
                    // Contras: mas consultas

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
                                            Foo f = new Foo(s,c,t,false);
                                            list.put(String.valueOf(id) + "/" + String.valueOf(id2) + "/" + String.valueOf(id3), f);

                                        }
                                    });
                                }
                                else {
                                    Foo f = new Foo(s,c,null,true);
                                    list.put(String.valueOf(id) + "/" + String.valueOf(id2) + "/" + 0, f);
                                }
                            }
                        });
                    }
                });
            }
        });

        return FXCollections.observableArrayList(list.values());
    }

}
