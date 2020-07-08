import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.*;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.beans.Observable;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestCatERing4 {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User u = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(u);
            System.out.println("TEST: Set Quantity.");
            // Deve essere caricato nella DB
            Event e = new Event(7,"San Fermin",new Date(2020,7,14));
            SummarySheet s = new SummarySheet(12,e,u);
            CatERing.getInstance().getKitchenTaskManager().chooseSheet(s);
            ObservableList<Slot> olist = CatERing.getInstance().getKitchenTaskManager().getShifts();
            ArrayList<Slot> slots = new ArrayList<>(olist);
            Recipe r = new Recipe(4,"Cotoletta","Cotoletta (a rigore) di vitello impanata e fritta nel burro.");
            Task t = CatERing.getInstance().getKitchenTaskManager().addRecipe(r);
            Slot slot = CatERing.getInstance().getKitchenTaskManager().assignTask(t,slots.get(0));
            System.out.println(slot);
            Slot changed = CatERing.getInstance().getKitchenTaskManager().setQuantityTask(slot,20);
            System.out.println(changed);
            System.out.println("TEST: Delete Task.");
            slot = CatERing.getInstance().getKitchenTaskManager().setTimeTask(changed,90);
            System.out.println(slot);
            //Deleting changes
            CatERing.getInstance().getKitchenTaskManager().setTimeTask(slot,0);
            CatERing.getInstance().getKitchenTaskManager().setQuantityTask(slot,1);
            CatERing.getInstance().getKitchenTaskManager().dischargeTask(changed);
            CatERing.getInstance().getKitchenTaskManager().deleteRecipe(r);

        } catch (UseCaseLogicException | SummarySheetException e) {
            e.printStackTrace();
            System.out.println("Errore di logica nello use case");
        }
    }
}