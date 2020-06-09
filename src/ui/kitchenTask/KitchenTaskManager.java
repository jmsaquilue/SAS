package ui.kitchenTask;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import ui.Main;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;

public class KitchenTaskManager {

    @FXML
    Label userLabel;

    @FXML
    BorderPane sheetListPane;

    @FXML
    SheetList sheetListPaneController;

    Main mainPaneController;

    public void initialize(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sheet-list.fxml"));
        try {
            sheetListPane = loader.load();
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String userName = CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
            userLabel.setText(userName);
        }

        sheetListPaneController.setParent(this);


    }


    public void setMainPaneController(Main main) {
        mainPaneController = main;
    }

}
