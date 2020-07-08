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

public class TestCatERing3 {
    public static void main(String[] args) {
        try {
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            User u = CatERing.getInstance().getUserManager().getCurrentUser();
            System.out.println(u);
            System.out.println("TEST: GetShift.");
            // Deve essere caricato nella DB
            Event e = new Event(7,"San Fermin",new Date(2020,7,14));
            SummarySheet s = new SummarySheet(12,e,u);
            CatERing.getInstance().getKitchenTaskManager().chooseSheet(s);
            ObservableList<Slot> olist = CatERing.getInstance().getKitchenTaskManager().getShifts();
            System.out.println(olist.toString());
            System.out.println("TEST: Assign Task.");
            Recipe r = new Recipe(4,"Cotoletta","Cotoletta (a rigore) di vitello impanata e fritta nel burro.");
            Task t = CatERing.getInstance().getKitchenTaskManager().addRecipe(r);
            ArrayList<Slot> slots = new ArrayList<>(olist);
            ArrayList<Slot> results = CatERing.getInstance().getKitchenTaskManager().assignTask(t,slots);
            System.out.println(results);
            System.out.println("TEST: Delete Task.");
            for (Slot slot: results){
                System.out.println(CatERing.getInstance().getKitchenTaskManager().dischargeTask(slot));
            }
            CatERing.getInstance().getKitchenTaskManager().deleteRecipe(r);
        } catch (UseCaseLogicException | SummarySheetException e) {
            e.printStackTrace();
            System.out.println("Errore di logica nello use case");
        }
    }
}