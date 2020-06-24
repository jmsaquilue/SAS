package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.kitchenTask.Slot;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.Task;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;

public class ShiftManager {
    @FXML
    Label sheetLabel;

    @FXML
    BorderPane containerPane;



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

}
