package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.Slot;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.kitchenTask.Task;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

public class ShiftManager {
    @FXML
    Label sheetLabel;

    @FXML
    DatePicker dataStart;

    @FXML
    DatePicker dataEnd;

    @FXML
    CheckBox completi;

    @FXML
    CheckBox disponibili;

    @FXML
    BorderPane containerPane;

    @FXML
    Button assegniButton;

    @FXML
    Button cancelliButton;

    @FXML
    Button quantity;
    @FXML
    Button time;

    @FXML
    ListView<Slot> shiftBoard;


    ObservableList<Slot> boardItems;

    ObservableList<Slot> filteredItems;

    SummarySheet currentSheet;

    KitchenTaskManager mainPaneController;


    //TODO: después de cada cambio llama a la función filter.



    public void initialize(SummarySheet sheet, Boolean b){
        currentSheet = sheet;
        sheetLabel.setText(currentSheet.toString());
        if (boardItems == null|| b){
            boardItems = CatERing.getInstance().getKitchenTaskManager().getShifts();
            shiftBoard.setItems(boardItems);
            shiftBoard.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            assegniButton.setDisable(true);
            quantity.setDisable(true);
            time.setDisable(true);
            cancelliButton.setDisable(true);
            //TODO: si has seleccionado uno ocupado, no te deja asignar compito

            shiftBoard.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Slot> c) -> {
                ArrayList<Slot> slist=new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
                assegniButton.setDisable(slist.size() == 0);
                quantity.setDisable(slist.size() != 1 || slist.get(0).getAvailable());
                time.setDisable(slist.size() != 1 || slist.get(0).getAvailable());
                cancelliButton.setDisable(slist.size()!=1 || slist.get(0).getAvailable());
                for(Slot s : slist){
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
                    Collections.sort(total);
                    boardItems = FXCollections.observableArrayList(total);
                    filter();

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
    public void editButtonQuantity(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Imposta Quantità");
        dialog.setContentText("Inserici una quantità:");

        Optional<String> result = dialog.showAndWait();
        int resultInt = 0;
        try {
            resultInt = Integer.parseInt(result.get());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (result.isPresent()){
            if (resultInt>0) {
                try {
                    ArrayList<Slot> old = new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
                    ArrayList<Slot> total = new ArrayList<Slot>(boardItems);
                    Slot newSlot = CatERing.getInstance().getKitchenTaskManager().setQuantityTask(old.get(0), resultInt);
                    total.remove(old.get(0));
                    total.add(newSlot);
                    Collections.sort(total);
                    boardItems = FXCollections.observableArrayList(total);
                    filter();
                } catch (UseCaseLogicException | SummarySheetException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("La quantità deve essere un numero positivo.");
                alert.showAndWait();
            }
        }
    }


    public void editButtonTime(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Imposta Time");
        dialog.setContentText("Inserici una quantità(minuti):");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        int resultInt = 0;
        try {
            resultInt = Integer.parseInt(result.get());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (result.isPresent()){
            if (resultInt>0) {
                try {
                    ArrayList<Slot> old = new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
                    ArrayList<Slot> total = new ArrayList<Slot>(boardItems);
                    Slot newSlot = CatERing.getInstance().getKitchenTaskManager().setTimeTask(old.get(0), resultInt);
                    total.remove(old.get(0));
                    total.add(newSlot);
                    Collections.sort(total);
                    boardItems = FXCollections.observableArrayList(total);
                    filter();
                } catch (UseCaseLogicException | SummarySheetException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Il tempo deve essere un numero positivo.");
                alert.showAndWait();
            }
        }
    }


    @FXML
    public void fineButtonPressed(){
        mainPaneController.openSheet(currentSheet);
    }

    public void cancelliButtonPressed(){
        ArrayList<Slot> old = new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
        if (old.size() == 1){
            try {
                ArrayList<Slot> total = new ArrayList<Slot>(boardItems);
                Slot newSlot = CatERing.getInstance().getKitchenTaskManager().dischargeTask(old.get(0));
                total.removeAll(old);
                total.add(newSlot);
                boardItems = FXCollections.observableArrayList(total);
                shiftBoard.setItems(boardItems);
                Collections.sort(total);
                boardItems = FXCollections.observableArrayList(total);
                filter();
            }catch (UseCaseLogicException | SummarySheetException ex) {
                ex.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Non rimane nenssun compito da assegnare.");
            alert.showAndWait();
        }
    }




    public void filter(){

        if (completi.isSelected() && disponibili.isSelected()){
            filteredItems = FXCollections.observableArrayList();
        }
        else {

            LocalDate dateStartOrig = dataStart.getValue();
            LocalDate dateEndOrig = dataEnd.getValue();
            Date start;
            Date end;

            if (dateStartOrig == null)
                start = new Date(Long.MIN_VALUE);
            else
                start = Date.from(dateStartOrig.atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (dateEndOrig == null)
                end = new Date(Long.MAX_VALUE);
            else
                end = Date.from(dateEndOrig.atStartOfDay(ZoneId.systemDefault()).toInstant());

            filteredItems = FXCollections.observableArrayList(boardItems);
            filteredItems.removeIf(n -> (n.getS().getDay().before(start) || n.getS().getDay().after(end)));
            if (completi.isSelected())
                filteredItems.removeIf(n -> n.getT() == null || (!(n.getT().getcomplete())));
            else if (disponibili.isSelected())
                filteredItems.removeIf(n -> (!(n.getAvailable())));
        }
        shiftBoard.setItems(filteredItems);
    }

}
