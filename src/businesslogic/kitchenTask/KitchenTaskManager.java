package businesslogic.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class KitchenTaskManager {
    private SummarySheet selectedSheet;
    private ArrayList<TaskEventReceiver> eventReceivers;

    public KitchenTaskManager(){
        this.eventReceivers = new ArrayList<>();
    }

    public SummarySheet chooseSheet(SummarySheet sheet) throws UseCaseLogicException, SummarySheetException {

        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        else if (!sheet.isCreator(user)){
            throw new SummarySheetException();
        }

        this.setSelectedSheet(sheet);

        return this.selectedSheet;
    }

    private void setSelectedSheet(SummarySheet selectedSheet) {
        this.selectedSheet = selectedSheet;
    }

    public SummarySheet createSheet(Event event) throws UseCaseLogicException, SummarySheetException {

        User user = CatERing.getInstance().getUserManager().getCurrentUser();


        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        else if(event.isRefered()){
            throw new SummarySheetException();
        }


        SummarySheet s = new SummarySheet(event,user);

        this.setSelectedSheet(s);
        this.notifySheetAdded(s);

        return s;
    }

    public SummarySheet getSelectedSheet() {
        return selectedSheet;
    }

    public Task addRecipe(Recipe r) throws UseCaseLogicException, SummarySheetException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();


        if(!user.isChef()){
            throw new UseCaseLogicException();
        }

        Task t = selectedSheet.createTask(r);

        this.notifyRecipeAdded(t);

        return t;
    }

    private void notifySheetAdded(SummarySheet s) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateSheetAdded(s);
        }
    }

    private void notifyRecipeAdded(Task t) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateRecipeAdded(t);
        }
    }

    public ObservableList<SummarySheet> getAllSummarySheets() {
        return SummarySheet.loadAllSummarySheets();
    }

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }
}
