package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class SheetList {

    private KitchenTaskManager kitchenTaskManagementController;

    ObservableList<SummarySheet> sheetListItems;


    @FXML
    ListView<SummarySheet> sheetList;


    @FXML
    public void nuovoButtonPressed(){
        //TODO: al pretar el boton te lleva a otra ventana.
        /*
        try{
           // CatERing.getInstance().getKitchenTaskManager().createSheet(currentEvent);
        }
        catch (UseCaseLogicException ex){
            ex.printStackTrace();
        }

         */

    }



    public void initialize() {
        if (sheetListItems == null) {
            // TODO: Mucho que hacer.
            sheetListItems = CatERing.getInstance().getKitchenTaskManager().getAllSummarySheets();
            sheetList.setItems(sheetListItems);
            sheetList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            /*
            sheetList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SummarySheet>() {
                @Override
                public void changed(ObservableValue<? extends Menu> observableValue, Menu oldMenu, Menu newMenu) {
                    User u = CatERing.getInstance().getUserManager().getCurrentUser();
                    eliminaButton.setDisable(newMenu == null || newMenu.isInUse() || !newMenu.isOwner(u));
                    apriButton.setDisable(newMenu == null || newMenu.isInUse() || !newMenu.isOwner(u));
                    copiaButton.setDisable(newMenu == null);
                }
            });

             */
        }
    }


    public void setParent(KitchenTaskManager kitchenTaskManager) {
        kitchenTaskManagementController = kitchenTaskManager;
    }
}
