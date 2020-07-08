import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;

import java.util.Date;

public class TestCatERing1 {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());
            System.out.println("TEST: Create a Summary sheet.");
            // Deve essere caricato nella DB
            Event e = new Event(7,"San Fermin",new Date(2020,7,14));
            SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createSheet(e);
            System.out.println("TEST: Choose that summary sheet.");
            System.out.println(CatERing.getInstance().getKitchenTaskManager().chooseSheet(s));

        } catch (UseCaseLogicException | SummarySheetException e) {
            e.printStackTrace();
            System.out.println("Errore di logica nello use case");
        }
    }
}