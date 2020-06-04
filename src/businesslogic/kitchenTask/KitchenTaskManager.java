package businesslogic.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.user.User;

import java.util.ArrayList;

public class KitchenTaskManager {
    private SummarySheet selectedSheet;
    private ArrayList<TaskEventReceiver> eventReceivers;

    public KitchenTaskManager(){
        this.eventReceivers = new ArrayList<>();
    }

    public SummarySheet chooseSheet(SummarySheet sheet) throws UseCaseLogicException{
        //User user = userMgr.getCurrentUser();
        // TODO: ponerlo bien, ahora esta en modo prueba

        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        else if (!sheet.isCreator(user)){
            // TODO: meter la excepción
        }

        this.setSelectedSheet(sheet);

        return this.selectedSheet;
    }

    private void setSelectedSheet(SummarySheet selectedSheet) {
        this.selectedSheet = selectedSheet;
    }

    public SummarySheet createSheet(Event event)  throws UseCaseLogicException{
        //User user = userMgr.getCurrentUser();
        // TODO: ponerlo bien, ahora esta en modo prueba

        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        else if(event.isRefered()){
            // TODO: meter la excepción
        }

        SummarySheet s = new SummarySheet(event,user);
        event.setRefered();

        this.notifySheetAdded(s);

        this.setSelectedSheet(s); // TODO: no se si habría que seleccionarlo

        return s;
    }

    private void notifySheetAdded(SummarySheet s) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateSheetAdded(s);
        }
    }


}
