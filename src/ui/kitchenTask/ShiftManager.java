package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.Slot;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.kitchenTask.Task;
import businesslogic.user.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Optional;

public class ShiftManager {
    @FXML
    Label sheetLabel;

    @FXML
    BorderPane containerPane;

    @FXML
    Button assegniButton;


    @FXML
    ListView<Slot> shiftBoard;


    ObservableList<Slot> boardItems;

    SummarySheet currentSheet;

    KitchenTaskManager mainPaneController;



    public void initialize(SummarySheet sheet){
        currentSheet = sheet;
        sheetLabel.setText(currentSheet.toString());

        if (boardItems == null){
            boardItems = CatERing.getInstance().getKitchenTaskManager().getShifts();
            System.out.println(boardItems);
            shiftBoard.setItems(boardItems);
            shiftBoard.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            //TODO: si has seleccionado uno ocupado, no te deja asignar compito

            shiftBoard.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Slot> c) -> {
                assegniButton.setDisable(false);
                for(Slot s : new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems())){
                    if (!s.getAvailable()) {
                        assegniButton.setDisable(true);
                        break;
                    }
                }
            });

            /*
            shiftBoard.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Slot>() {
                @Override
                public void changed(ObservableValue<? extends Slot> observableValue) {
                    for (Slot )
                    User u = CatERing.getInstance().getUserManager().getCurrentUser();
                    assegniButton.setDisable(newMenu == null || newMenu.isInUse() || !newMenu.isOwner(u));
                    //apriButton.setDisable(newMenu == null || newMenu.isInUse() || !newMenu.isOwner(u));
                    //copiaButton.setDisable(newMenu == null);
                }
            });*/
        }


        /*
        inicRecipeListViewItems = CatERing.getInstance().getRecipeManager().getAllSelectedRecipes(currentSheet);
        selectedRecipeListViewItems = FXCollections.observableArrayList(inicRecipeListViewItems);
        selectedRecipeListView.setItems(inicRecipeListViewItems);
        selectedRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);*/
    }

    public void setKitchenTaskManagerController(KitchenTaskManager kitchenTaskManager) {
        mainPaneController = kitchenTaskManager;
    }

    public void assegniButtonPressed(){
        ArrayList<Task> choices = CatERing.getInstance().getKitchenTaskManager().getAvailableTask();

        if (choices.size() > 0) {
            ChoiceDialog<Task> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Assegni compito.");
            dialog.setHeaderText("Assegni compito.");
            dialog.setContentText("Scegli il compito da assegnare:");

            Optional<Task> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    ArrayList<Slot> old = new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
                    ArrayList<Slot> total = new ArrayList<Slot>(boardItems);
                    total.removeAll(old);
                    if (shiftBoard.getSelectionModel().getSelectedItems().size() > 1){
                        ArrayList<Slot> newSlots = CatERing.getInstance().getKitchenTaskManager().assignTask(result.get(),old);
                        total.addAll(newSlots);
                    }
                    else {
                        Slot newSlot = CatERing.getInstance().getKitchenTaskManager().assignTask(result.get(),old.get(0));
                        total.add(newSlot);
                    }
                    boardItems = FXCollections.observableArrayList(total);
                    shiftBoard.setItems(boardItems);

                } catch (UseCaseLogicException | SummarySheetException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Non rimane nenssun compito da assegnare.");
            alert.showAndWait();
        }
    }


    @FXML
    public void fineButtonPressed(){
        mainPaneController.openSheet(currentSheet);
    }

}
