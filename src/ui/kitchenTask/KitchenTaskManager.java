package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.kitchenTask.SummarySheet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import ui.Main;
import javafx.scene.control.Label;

import java.io.IOException;

public class KitchenTaskManager {

    @FXML
    Label userLabel;

    @FXML
    BorderPane sheetListPane;

    @FXML
    BorderPane containerPane;

    @FXML
    SheetList sheetListPaneController;

    @FXML
    BorderPane recipeContentPane;

    @FXML
    BorderPane shiftPane;

    @FXML
    RecipeContent recipePaneController;

    Main mainPaneController;

    @FXML
    ShiftManager shiftPaneController;


    public void initialize(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("recipe-manager.fxml"));
        try {
            recipeContentPane = loader.load();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        recipePaneController = loader.getController();
        recipePaneController.setKitchenTaskManagerController(this);


        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("shift-manager.fxml"));
        try {
            shiftPane = loader2.load();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        shiftPaneController = loader2.getController();
        shiftPaneController.setKitchenTaskManagerController(this);

        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String userName = CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
            userLabel.setText(userName);
        }

        sheetListPaneController.setParent(this);


    }



    public void setMainPaneController(Main main) {
        mainPaneController = main;
    }

    public void endKitchenTaskManager() {
        mainPaneController.showStartPane();
    }


    public void openSheet(SummarySheet sheet) {
        recipePaneController.initialize(sheet);
        containerPane.setCenter(recipeContentPane);
    }

    public void showList() {
        sheetListPaneController.initialize();
        containerPane.setCenter(sheetListPane);
    }

    public void showShift(SummarySheet sheet) {
        shiftPaneController.initialize(sheet);
        containerPane.setCenter(shiftPane);
    }

}
