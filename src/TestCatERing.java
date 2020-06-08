import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.user.User;
import persistence.PersistenceManager;

import java.util.Date;

public class TestCatERing {
    public static void main(String[] args) {
        try {
            //PersistenceManager.testSQLConnection();
            CatERing.getInstance().getUserManager().fakeLogin("Paola");
            //Event event = new Event("Guaza de tamango",new Date(2020,6-1,4) ); //quien coño cuenta los meses empezando por 0??
            //SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createSheet(event);
            //System.out.println(s.toString());
        }
        catch (Exception e){
            System.out.println("Error");
        }
    }

}
