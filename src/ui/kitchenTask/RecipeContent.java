package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class RecipeContent {
    @FXML
    Label sheetLabel;

    @FXML
    GridPane recipeview;

    @FXML
    BorderPane containerPane;

    ObservableList<Recipe> recipeListViewItems;
    ObservableList<Recipe> selectedRecipeListViewItems;

    @FXML
    ListView<Recipe> recipeListView;
    @FXML
    ListView<Recipe> selectedRecipeListView;

    @FXML
    FlowPane fw;
    KitchenTaskManager mainPaneController;
    SummarySheet currentSheet;

    Boolean sheetchangeshift;
    Boolean sheetchange;


    public void initialize(SummarySheet sheet){

        currentSheet = sheet;
        sheetLabel.setText(currentSheet.toString());
        if (recipeListViewItems == null || sheetchange) {
            recipeListViewItems = CatERing.getInstance().getRecipeManager().getAllAvailableRecipes(currentSheet);
            recipeListView.setItems(recipeListViewItems);
            recipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }

        if (selectedRecipeListViewItems == null || sheetchange){
            selectedRecipeListViewItems = CatERing.getInstance().getRecipeManager().getAllSelectedRecipes(currentSheet);
            selectedRecipeListView.setItems(selectedRecipeListViewItems);
            selectedRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            sheetchange=false;
        }

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
        sheetchange=true;
        sheetchangeshift=true;
    }

    public void turniButtonPressed(){
        mainPaneController.showShift(currentSheet, sheetchangeshift);
        sheetchangeshift=false;
    }

}
