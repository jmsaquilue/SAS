package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.Slot;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Optional;

public class SheetList {

    private KitchenTaskManager kitchenTaskManagementController;

    ObservableList<SummarySheet> sheetListItems;

    @FXML
    Button apriButton;

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
            ImageView img = new ImageView(this.getClass().getResource("../img/logo.png").toString());
            img.setFitHeight(120);
            img.setFitWidth(120);
            dialog.setGraphic(img);
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
            kitchenTaskManagementController.openSheet(s);
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
            sheetListItems = CatERing.getInstance().getKitchenTaskManager().getAllSummarySheets();
            sheetList.setItems(sheetListItems);
            sheetList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            apriButton.setDisable(true);
            sheetList.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends SummarySheet> c) -> {
                apriButton.setDisable(false);
            });
        }
    }


    public void setParent(KitchenTaskManager kitchenTaskManager) {
        kitchenTaskManagementController = kitchenTaskManager;
    }
}
