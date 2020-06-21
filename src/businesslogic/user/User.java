package businesslogic.user;

import javafx.collections.FXCollections;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {


    public static enum Role{SERVIZIO,CUOCO,CHEF,ORGANIZZATORE}
    private int id;
    private String username;
    private Set<Role> roles;
    private String password;
    private String name;
    private String surname;
    private String gender;
    private int age;
    private String email;

    private static Map<Integer, User> loadedUsers = FXCollections.observableHashMap();

    public User(){
        id = 0;
        username = "";
        this.roles = new HashSet<>();
    }

    public User(int id, String username, String password, String name, String surname, String gender, int age, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    @Override
    public String toString() {
        String result = username;
        if (roles.size() > 0) {
            result += ": ";

            for (User.Role r : roles) {
                result += r.toString() + " ";
            }
        }
        return result;
    }


    public boolean isChef() {
        return roles.contains(Role.CHEF);
    }

    public int getId(){ return id;}

    public String getUserName(){
        return username;
    }


    public static User loadUserById(int id) {

        if (loadedUsers.containsKey(id)) return loadedUsers.get(id);

        User u = new User();
        String userQuery = "SELECT * FROM Users WHERE id='"+id+"'";
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
                u.password = rs.getString("pass");
                u.name = rs.getString("name");
                u.surname = rs.getString("surname");
                u.gender = rs.getString("gender");
                u.age = rs.getInt("age");
                u.email = rs.getString("email");
            }
        });
        if (u.id > 0) {
            loadedUsers.put(u.id, u);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + u.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            u.roles.add(User.Role.CUOCO);
                            break;
                        case 'h':
                            u.roles.add(User.Role.CHEF);
                            break;
                        case 'o':
                            u.roles.add(User.Role.ORGANIZZATORE);
                            break;
                        case 's':
                            u.roles.add(User.Role.SERVIZIO);
                    }
                }
            });
        }
        return u;
    }

    public static User loadUser(String username){
        User u = new User();

        String userQuery = "SELECT * FROM Users WHERE username='"+username+"';";
        PersistenceManager.executeQuery(userQuery, new ResultHandler(){
            @Override
            public void handle(ResultSet rs) throws SQLException{
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
                u.password = rs.getString("pass");
                u.name = rs.getString("name");
                u.surname = rs.getString("surname");
                u.gender = rs.getString("gender");
                u.age = rs.getInt("age");
                u.email = rs.getString("email");
            }
        });

        if (u.id > 0){
            userQuery = "SELECT * FROM UserRoles WHERE user_id='"+u.id+"';";
            PersistenceManager.executeQuery(userQuery, new ResultHandler(){

                @Override
                public void handle(ResultSet rs) throws SQLException{
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)){
                        case 'c':
                            u.roles.add(Role.CUOCO);
                            break;
                        case 'h':
                            u.roles.add(Role.CHEF);
                            break;
                        case 'o':
                            u.roles.add(Role.ORGANIZZATORE);
                            break;
                        case 's':
                            u.roles.add(Role.SERVIZIO);
                            break;
                    }
                }
            });
        }


        return u;
    }



}
