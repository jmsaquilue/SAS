package persistence;

import businesslogic.kitchenTask.*;

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
    public void updateTaskAssigned(Task t, Slot slot){
        ShiftBoard.saveChoose(t,slot);

    }
}
