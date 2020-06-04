package businesslogic.user;

public class User {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String gender;
    private int age;
    private String email;

    @Override
    public String toString() {
        return this.name + " " + this.surname;
    }

    public boolean isChef() {
        // TODO: programar esto
        return true;
    }
}
