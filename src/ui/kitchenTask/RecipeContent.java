package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class RecipeContent {
    @FXML
    Label sheetLabel;

    @FXML
    GridPane recipeview;

    ObservableList<Recipe> recipeListViewItems;


    @FXML
    ListView<Recipe> recipeListView;

    @FXML
    FlowPane fw;
    KitchenTaskManager mainPaneController;

    public void initialize(){
        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String info = CatERing.getInstance().getKitchenTaskManager().getSelectedSheet().toString();
            System.out.println(info);
            sheetLabel.setText(info);
        }

        recipeListViewItems = CatERing.getInstance().getRecipeManager().getAllRecipes();
        recipeListView.setItems(recipeListViewItems);
        recipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public void setKitchenTaskManagerController(KitchenTaskManager kitchenTaskManager) {
        mainPaneController = kitchenTaskManager;
    }

    @FXML
    public void infoButtonPressed(){
        Recipe r = recipeListView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazioni sulla ricetta.");
        alert.setHeaderText(r.toString());
        alert.setContentText(r.getDescription());

        alert.showAndWait();
    }

    @FXML
    public void fineButtonPressed(){
        mainPaneController.showList();
    }

}
