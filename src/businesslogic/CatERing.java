package businesslogic;

import businesslogic.kitchenTask.KitchenTaskManager;
import businesslogic.user.UserManager;
import persistence.SummarySheetPersistence;

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

    private SummarySheetPersistence summarySheetPersistence;

    private CatERing(){
        ktMgr = new KitchenTaskManager();
        userMgr = new UserManager();
        summarySheetPersistence = new SummarySheetPersistence();
        ktMgr.addEventReceiver(summarySheetPersistence);

    }

    public KitchenTaskManager getKitchenTaskManager() {
        return ktMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }
}
