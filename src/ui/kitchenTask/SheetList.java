package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class SheetList {

    private KitchenTaskManager kitchenTaskManagementController;

    ObservableList<SummarySheet> sheetListItems;


    @FXML
    ListView<SummarySheet> sheetList;



    @FXML
    public void nuovoButtonPressed(){
        ArrayList<Event> choices = CatERing.getInstance().getEventManager().getAvailableEvents();

        if (choices.size() > 0) {
            ChoiceDialog<Event> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Nuovo foglio riepilogativo.");
            dialog.setHeaderText("Nuovo foglio riepilogativo.");
            dialog.setContentText("scegli l'evento da organizzare:");

            Optional<Event> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    SummarySheet s = CatERing.getInstance().getKitchenTaskManager().createSheet(result.get());
                    sheetListItems.add(s);
                } catch (UseCaseLogicException | SummarySheetException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Non ci sono eventi disponibili.");
            alert.showAndWait();
        }
    }


    @FXML
    public void apriButtonPressed(){
        try {
            SummarySheet s = sheetList.getSelectionModel().getSelectedItem();
            CatERing.getInstance().getKitchenTaskManager().chooseSheet(s);
            kitchenTaskManagementController.openSheet();
        } catch (UseCaseLogicException | SummarySheetException ex){
            ex.printStackTrace();
        }

    }

    @FXML
    public void fineButtonPressed(){
        kitchenTaskManagementController.endKitchenTaskManager();
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
