package businesslogic;

import businesslogic.kitchenTask.KitchenTaskManager;
import businesslogic.user.UserManager;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance(){
        if (singleInstance == null){
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private KitchenTaskManager ktMgr;
    private UserManager userMgr;

    private CatERing(){
        ktMgr = new KitchenTaskManager();
        userMgr = new UserManager();

    }

    public KitchenTaskManager getKitchenTaskManager() {
        return ktMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }
}
