package businesslogic.user;

import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
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


    public User(){
        id = 0;
        username = "";
        this.roles = new HashSet<>();
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

    public String getUserName(){
        return username;
    }


    public static User loadUser(String username){
        User u = new User();

        String userQuery = "SELECT * FROM Users WHERE username='"+username+"';";
        PersistenceManager.executeQuery(userQuery, new ResultHandler(){
            @Override
            public void handle(ResultSet rs) throws SQLException{
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
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

        System.out.println(u);

        return u;
    }



}
