package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.kitchenTask.KitchenTaskManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.user.UserManager;
import persistence.SummarySheetPersistence;

;

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
    private EventManager eventMgr;
    private RecipeManager recipeMgr;

    private SummarySheetPersistence summarySheetPersistence;

    private CatERing(){
        ktMgr = new KitchenTaskManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        recipeMgr = new RecipeManager();

        summarySheetPersistence = new SummarySheetPersistence();
        ktMgr.addEventReceiver(summarySheetPersistence);

    }

    public KitchenTaskManager getKitchenTaskManager() {
        return ktMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() {
        return eventMgr;
    }

    public RecipeManager getRecipeManager(){return  recipeMgr;}

}
