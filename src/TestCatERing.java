import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.Task;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;

import java.util.ArrayList;
import java.util.Date;

public class TestCatERing {
    public static void main(String[] args) {
        try {
            System.out.println("TEST DATABASE CONNECTION");
            //PersistenceManager.testSQLConnection();
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());
            System.out.println("TEST SELECTION");
            // Importante avere al più un sheet caricato nella DB
            ObservableList<SummarySheet> ss = CatERing.getInstance().getKitchenTaskManager().getAllSummarySheets();
            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().chooseSheet(ss.get(0));
            System.out.println(s);
            System.out.println("TEST ADD RECIPE");

            // TODO: metodo para escribir por pantalla un task
        }
        catch (Exception e){
            System.out.println("Error");
        }
    }

}
