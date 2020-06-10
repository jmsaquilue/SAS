package persistence;

import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.TaskEventReceiver;

public class SummarySheetPersistence implements TaskEventReceiver {


    @Override
    public void updateSheetAdded(SummarySheet s) {
        System.out.println("AQUI ENTRA.");
        SummarySheet.saveNewSheet(s);
    }
}
