package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.kitchenTask.*;
import businesslogic.user.Cook;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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
    Button changeCookButton;

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
            disponibili.setSelected(false);
            completi.setSelected(false);
            changeCookButton.setDisable(true);

            shiftBoard.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Slot> c) -> {
                ArrayList<Slot> slist=new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
                assegniButton.setDisable(slist.size() == 0);
                quantity.setDisable(slist.size() != 1 || slist.get(0).getAvailable());
                changeCookButton.setDisable(slist.size() != 1 || slist.get(0).getAvailable());
                time.setDisable(slist.size() != 1 || slist.get(0).getAvailable());
                cancelliButton.setDisable(slist.size()!=1 || slist.get(0).getAvailable());

                for(Slot s : slist){
                    if (!s.getAvailable()) {
                        assegniButton.setDisable(true);
                        break;
                    }
                }
            });

        }

    }

    public void setKitchenTaskManagerController(KitchenTaskManager kitchenTaskManager) {
        mainPaneController = kitchenTaskManager;
    }


    private class Result{
        private Task t;
        private Cook c;
        private boolean setCook;

        Result(Task t, Cook c, boolean senza){
            this.t = t;
            this.setCook = !senza;
            if (setCook){
                this.c = c;
            }
            else {
                this.c = null;
            }
        }

        public Task getT() {
            return t;
        }

        public Cook getC(){
            return c;
        }

        public boolean getSetCook(){
            return setCook;
        }
    }

    public void assegniButtonPressed(){
        ArrayList<Shift> shifts = new ArrayList<>();
        for (Slot s : shiftBoard.getSelectionModel().getSelectedItems())
            shifts.add(s.getS());

        ArrayList<Task> choices = CatERing.getInstance().getKitchenTaskManager().getAvailableTask(shifts);
        ArrayList<Cook> cooks = null;

        cooks = shiftBoard.getSelectionModel().getSelectedItems().get(0).getS().getCooks();



        if (choices.size() > 0 && (shiftBoard.getSelectionModel().getSelectedItems().size() > 1 || cooks.size() > 0)) {
            Dialog<Result> dialog = new Dialog<>();
            dialog.setTitle("Assegni compito.");
            dialog.setHeaderText("Assegni compito.");

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            ComboBox<Task> choiceTask = new ComboBox<>(FXCollections.observableArrayList(choices));
            choiceTask.getSelectionModel().selectFirst();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Scegli il compito da assegnare:"), 0, 0);
            grid.add(choiceTask, 1, 0);
            Label cookLAbel = new Label("Scegli il cuoco incaricato (opzionale):");
            ComboBox<Cook> choiceCooks = new ComboBox<>(FXCollections.observableArrayList(cooks));
            CheckBox cb = new CheckBox("Senza Cuoco");
            choiceCooks.getSelectionModel().selectFirst();

            cb.setOnAction((evt) -> {
                choiceCooks.setVisible(!cb.isSelected());
                cookLAbel.setVisible(!cb.isSelected());
            });

            if (shiftBoard.getSelectionModel().getSelectedItems().size() == 1) {
                grid.add(cookLAbel, 0, 1);
                grid.add(choiceCooks, 1, 1);
                grid.add(cb, 0, 2);
            }


            dialog.getDialogPane().setContent(grid);
            dialog.setResultConverter((ButtonType button) -> {
                if (button == ButtonType.OK) {
                    if (shiftBoard.getSelectionModel().getSelectedItems().size() == 1) {
                        return new Result(choiceTask.getValue(), choiceCooks.getValue(), cb.isSelected());
                    }
                    else {
                        return new Result(choiceTask.getValue(), null, false);
                    }
                }
                return null;
            });

            Optional<Result> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    ArrayList<Slot> old = new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
                    ArrayList<Slot> total = new ArrayList<Slot>(boardItems);
                    total.removeAll(old);
                    if (shiftBoard.getSelectionModel().getSelectedItems().size() > 1){
                        ArrayList<Slot> newSlots = CatERing.getInstance().getKitchenTaskManager().assignTask(result.get().getT(),old);
                        for (Slot s: old) {
                            if (!s.getS().isFull()) {
                                Slot copy = new Slot(s.getS(), null, null, true);
                                total.add(copy);
                            }
                        }
                        total.addAll(newSlots);
                    }
                    else {
                        Slot newSlot = null;

                        if (result.get().getSetCook()) {
                            newSlot = CatERing.getInstance().getKitchenTaskManager().assignTask(result.get().getT(), old.get(0),
                            result.get().getC());
                        }
                        else{
                            newSlot = CatERing.getInstance().getKitchenTaskManager().assignTask(result.get().getT(), old.get(0));
                        }
                        if (!old.get(0).getS().isFull()){
                            Slot copy = new Slot(old.get(0).getS(),null,null,true);
                            total.add(copy);
                        }
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
    public void changeCookButtonPressed(){
        ArrayList<Cook> cooks = shiftBoard.getSelectionModel().getSelectedItems().get(0).getS().getCooks();

        System.out.println(cooks);
        Dialog<Result> dialog = new Dialog<>();

        dialog.setTitle("Cambi cuoco.");
        dialog.setHeaderText("Cambi cuoco.");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        Label cookLAbel = new Label("Scegli il cuoco incaricato:");
        ComboBox<Cook> choiceCooks = new ComboBox<>(FXCollections.observableArrayList(cooks));
        CheckBox cb = new CheckBox("Senza Cuoco");
        choiceCooks.getSelectionModel().selectFirst();

        cb.setOnAction((evt) -> {
                    choiceCooks.setVisible(!cb.isSelected());
                    cookLAbel.setVisible(!cb.isSelected());
                });

        if (cooks.size() >0 ) {
            grid.add(cookLAbel, 0, 0);
            grid.add(choiceCooks, 1, 0);
        }
        grid.add(cb, 0, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                if (cb.isSelected() || cooks.size() == 0) {
                    return new Result(null, null, cb.isSelected());
                }
                else {
                    return new Result(null, choiceCooks.getValue(), cb.isSelected());

                }
            }
            return null;
        });

        Optional<Result> result = dialog.showAndWait();

        try {
            ArrayList<Slot> old = new ArrayList<Slot>(shiftBoard.getSelectionModel().getSelectedItems());
            ArrayList<Slot> total = new ArrayList<Slot>(boardItems);
            Slot newSlot = CatERing.getInstance().getKitchenTaskManager().setCook(old.get(0), result.get().getC());
            total.remove(old.get(0));
            total.add(newSlot);
            Collections.sort(total);
            boardItems = FXCollections.observableArrayList(total);
            filter();
        } catch (UseCaseLogicException | SummarySheetException ex) {
            ex.printStackTrace();
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
                boolean wasFull = old.get(0).getS().isFull();
                Slot newSlot = CatERing.getInstance().getKitchenTaskManager().dischargeTask(old.get(0));
                total.removeAll(old);
                if (wasFull)
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
