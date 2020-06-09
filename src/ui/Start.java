package ui;

import javafx.fxml.FXML;

public class Start{

    private Main mainPaneController;

    @FXML
    void beginKitchenTaskManager() {

        mainPaneController.startKitchenTaskManager();

    }

    public void setParent(Main main){
        this.mainPaneController = main;
    }
}
