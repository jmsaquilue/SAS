import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.kitchenTask.Task;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;

import java.util.Date;

public class TestCatERing2 {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User u = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(u);
            System.out.println("TEST: Add recipe.");
            // Deve essere caricato nella DB
            Event e = new Event(7,"San Fermin",new Date(2020,7,14));
            SummarySheet s = new SummarySheet(12,e,u);
            CatERing.getInstance().getKitchenTaskManager().chooseSheet(s);
            Recipe r = new Recipe(4,"Cotoletta","Cotoletta (a rigore) di vitello impanata e fritta nel burro.");
            Task t = CatERing.getInstance().getKitchenTaskManager().addRecipe(r);
            System.out.println(t.show());
            System.out.println("TEST: Delete recipes.");
            CatERing.getInstance().getKitchenTaskManager().deleteRecipe(r);
        } catch (UseCaseLogicException | SummarySheetException e) {
            e.printStackTrace();
            System.out.println("Errore di logica nello use case");
        }
    }
}