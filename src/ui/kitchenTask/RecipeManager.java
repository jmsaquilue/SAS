package ui.kitchenTask;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class RecipeManager {
    @FXML
    Label sheetLabel;

    @FXML
    GridPane recipeView;
    @FXML
    FlowPane fw;
    KitchenTaskManager mainPaneController;
    RecipeView recipelist;

    public void initialize(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("recipe-view.fxml"));
        try {
            recipelist = loader.load();
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String info = CatERing.getInstance().getKitchenTaskManager().getSelectedSheet().toString();
            sheetLabel.setText(info);
        }

        recipelist.setParent(this);


    }
    /*public void setMainPaneController(KitchenTaskManager kt) {
        mainPaneController = kt;
    }
    public void endKitchenTaskManager() {
        mainPaneController.showStartPane();
    }*/
    public void setParent(KitchenTaskManager kt) {
        mainPaneController = kt;
    }

}
