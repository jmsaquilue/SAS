package businesslogic.user;

public class UserManager {
    private User currentUser;

    public void fakeLogin(){
        // TODO: hay que crear el login de verdad
        this.currentUser = new User();
    }

    public User getCurrentUser() {
        return this.currentUser;
    }
}
