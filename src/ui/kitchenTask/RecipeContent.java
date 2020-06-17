package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.recipe.Recipe;
import businesslogic.recipe.RecipeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class RecipeContent {
    @FXML
    Label sheetLabel;

    @FXML
    GridPane recipeview;

    ObservableList<Recipe> recipeListViewItems;
    ObservableList<Recipe> selectedRecipeListViewItems;
    ObservableList<Recipe> inicRecipeListViewItems;

    @FXML
    ListView<Recipe> recipeListView;
    @FXML
    ListView<Recipe> selectedRecipeListView;

    @FXML
    FlowPane fw;
    KitchenTaskManager mainPaneController;
    SummarySheet currentSheet;

    public void initialize(SummarySheet sheet){
        currentSheet = sheet;
        sheetLabel.setText(currentSheet.toString());
        recipeListViewItems = CatERing.getInstance().getRecipeManager().getAllAvailableRecipes(currentSheet);
        recipeListView.setItems(recipeListViewItems);
        recipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        inicRecipeListViewItems = CatERing.getInstance().getRecipeManager().getAllSelectedRecipes(currentSheet);
        selectedRecipeListViewItems = FXCollections.observableArrayList(inicRecipeListViewItems);
        selectedRecipeListView.setItems(inicRecipeListViewItems);
        selectedRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public void setKitchenTaskManagerController(KitchenTaskManager kitchenTaskManager) {
        mainPaneController = kitchenTaskManager;
    }

    @FXML
    public void addButtonPressed(){
        Recipe r = recipeListView.getSelectionModel().getSelectedItem();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Aggiungi ricetta.");
        alert.setHeaderText(null);
        alert.setContentText("Vuole davvero aggiungere "+r.toString()+" al foglio relativo a "+currentSheet.getEvent().toString() +"?");

        // parece que hoy funciona. Lastima que no me haga falta
        //alert.setGraphic(new ImageView(this.getClass().getResource("../logo2.png").toString()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                CatERing.getInstance().getKitchenTaskManager().addRecipe(r);
                recipeListViewItems.remove(r);
                selectedRecipeListViewItems.add(r);
                recipeListView.setItems(recipeListViewItems);
                selectedRecipeListView.setItems(selectedRecipeListViewItems);
            }catch (UseCaseLogicException| SummarySheetException e){
                e.printStackTrace();
            }


        }



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
