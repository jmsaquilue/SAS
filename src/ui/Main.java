package ui;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ui.kitchenTask.KitchenTaskManager;
import ui.kitchenTask.RecipeContent;

import java.io.IOException;

public class Main {

    @FXML
    private AnchorPane paneContainer;

    @FXML
    private FlowPane startPane;

    @FXML
    Start startPaneController;

    BorderPane kitchenTaskManagementPane;
    KitchenTaskManager kitchenTaskManagementPaneController;
    RecipeContent recipeContentPaneController;

    public void initialize() {
        startPaneController.setParent(this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("kitchenTask/kitchen-task-manager.fxml"));
        try {
            kitchenTaskManagementPane = loader.load();
            kitchenTaskManagementPaneController = loader.getController();
            kitchenTaskManagementPaneController.setMainPaneController(this);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void startKitchenTaskManager() {
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");
        kitchenTaskManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(kitchenTaskManagementPane);
        AnchorPane.setTopAnchor(kitchenTaskManagementPane, 0.0);
        AnchorPane.setBottomAnchor(kitchenTaskManagementPane, 0.0);
        AnchorPane.setLeftAnchor(kitchenTaskManagementPane, 0.0);
        AnchorPane.setRightAnchor(kitchenTaskManagementPane, 0.0);

    }

    public void showStartPane() {
        paneContainer.getChildren().remove(kitchenTaskManagementPane);
        paneContainer.getChildren().add(startPane);
    }

}
