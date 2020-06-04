import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;

import java.util.Date;

public class TestCatERing {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().fakeLogin();
            Event event = new Event("Guaza de tamango",new Date(2020,6-1,4) ); //quien coño cuenta los meses empezando por 0??
            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createSheet(event);
            System.out.println(s.toString());
        }
        catch (UseCaseLogicException e){
            System.out.println("Error");
        }
    }

}
