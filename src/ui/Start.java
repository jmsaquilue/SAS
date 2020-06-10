package ui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;


import javafx.scene.image.ImageView;

public class Start{

    private Main mainPaneController;

    @FXML
    private ImageView imageView;

    @FXML
    void beginKitchenTaskManager() {
        mainPaneController.startKitchenTaskManager();
    }


    public void setParent(Main main){
        this.mainPaneController = main;
    }
}
