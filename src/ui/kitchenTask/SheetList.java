package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import javafx.fxml.FXML;

public class SheetList {

    Event currentEvent;

    @FXML
    public void nuovoButtonPressed(){
        //TODO: al pretar el boton te lleva a otra ventana.

        try{
            CatERing.getInstance().getKitchenTaskManager().createSheet(currentEvent);
        }
        catch (UseCaseLogicException ex){
            ex.printStackTrace();
        }

    }

}
