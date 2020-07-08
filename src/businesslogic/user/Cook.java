package businesslogic.user;

import businesslogic.kitchenTask.Shift;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cook extends User{


    public Cook(int id, String username, String password, String name, String surname, String gender, int age, String email) {
        super(id,username,password,name,surname,gender,age,email);
    }


    public Boolean availableShift(Shift s) {
        String querry = "SELECT COUNT(*) FROM CookShifts WHERE shift_id='"+s.getId()+"' and cook_id='"+this.id+"';";
        final Boolean[] available = {false};
        PersistenceManager.executeQuery(querry, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int count = rs.getInt("COUNT(*)");
                available[0] = (count>0);
            }
        });
        if (available[0]) {
            String querry2 = "SELECT COUNT(*) FROM TaskCookShifts WHERE shift_id='" + s.getId() + "' and cook_id='" + this.id + "';";
            PersistenceManager.executeQuery(querry2, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    int count = rs.getInt("COUNT(*)");
                    available[0] = (count == 0);
                }
            });
        }
        return available[0];
    }

    public String toString(){
        return getUserName();
    }
}
