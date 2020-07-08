package persistence;

import businesslogic.kitchenTask.*;
import businesslogic.recipe.Recipe;

public class SummarySheetPersistence implements TaskEventReceiver {


    @Override
    public void updateSheetAdded(SummarySheet s) {
        SummarySheet.saveNewSheet(s);
    }

    @Override
    public void updateRecipeAdded(Task t){
        Task.saveTask(t);
    }

    @Override
    public void updateRecipeDeleted(Recipe r, SummarySheet sheet){
        Task.deleteRecipe(r,sheet);
    }

    @Override
    public void updateTaskAssigned(Slot slot){
        ShiftBoard.saveChoose(slot);
    }

    @Override
    public void updateTaskQuantity(Task t){
        Task.saveQuantity(t);
    }

    @Override
    public void updateTaskTime(Task t){
        Task.saveTime(t);
    }

    @Override
    public void updateTaskdischarged(Slot slot){
        ShiftBoard.dropChoose(slot);
    }

}
