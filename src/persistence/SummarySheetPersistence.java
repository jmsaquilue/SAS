package persistence;

import businesslogic.kitchenTask.*;
import ui.kitchenTask.ShiftManager;

public class SummarySheetPersistence implements TaskEventReceiver {


    @Override
    public void updateSheetAdded(SummarySheet s) {
        SummarySheet.saveNewSheet(s);
    }

    @Override
    public void updateRecipeAdded(Task t){
        //TODO:
    }

    @Override
    public void updateTaskAssigned(Task t, Slot slot){
        ShiftBoard.saveChoose(t,slot);

    }
}
