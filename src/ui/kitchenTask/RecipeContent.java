package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchenTask.SummarySheet;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.recipe.Recipe;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class RecipeContent {
    @FXML
    Label sheetLabel;

    @FXML
    Button addButton;

    @FXML
    Button infoButton;
    @FXML
    Button infoButton2;

    @FXML
    Button deleteButton;

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
        addButton.setDisable(true);
        infoButton.setDisable(true);
        infoButton2.setDisable(true);
        deleteButton.setDisable(true);
        recipeListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Recipe> c) -> {
            addButton.setDisable(recipeListView.getSelectionModel().getSelectedItems().isEmpty());
            infoButton.setDisable(recipeListView.getSelectionModel().getSelectedItems().isEmpty());
        });
        selectedRecipeListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends Recipe> c) -> {
            deleteButton.setDisable(selectedRecipeListView.getSelectionModel().getSelectedItems().isEmpty());
            infoButton2.setDisable(selectedRecipeListView.getSelectionModel().getSelectedItems().isEmpty());
        });

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
        ImageView img = new ImageView(this.getClass().getResource("../img/logo.png").toString());
        img.setFitHeight(120);
        img.setFitWidth(120);
        alert.setGraphic(img);

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

    public void getInfo(Recipe r){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazioni sulla ricetta.");
        alert.setHeaderText(r.toString());

        Text text = new Text(r.getDescription());
        text.setFont(new Font(14));
        text.setWrappingWidth(250);
        text.setTextAlignment(TextAlignment.CENTER);
        alert.getDialogPane().setContent(text);

        ImageView img = new ImageView(this.getClass().getResource("../img/logo.png").toString());
        img.setFitHeight(120);
        img.setFitWidth(120);
        alert.setGraphic(img);
        alert.showAndWait();
    }

    @FXML
    public void infoButtonPressed(){
        Recipe r = recipeListView.getSelectionModel().getSelectedItem();
        getInfo(r);

    }

    @FXML
    public void infoButton2Pressed(){
        Recipe r = selectedRecipeListView.getSelectionModel().getSelectedItem();
        getInfo(r);
    }

    @FXML
    public void deleteButtonPressed(){
        Recipe r = selectedRecipeListView.getSelectionModel().getSelectedItem();


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Rimuovere ricetta.");
        alert.setHeaderText(null);
        alert.setContentText("Vuole davvero rimuovere "+r.toString()+" al foglio relativo a "+currentSheet.getEvent().toString() +"?");
        ImageView img = new ImageView(this.getClass().getResource("../img/logo.png").toString());
        img.setFitHeight(120);
        img.setFitWidth(120);
        alert.setGraphic(img);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try {
                if (CatERing.getInstance().getKitchenTaskManager().deleteRecipe(r)){
                    selectedRecipeListViewItems.remove(r);
                    recipeListViewItems.add(r);
                    recipeListView.setItems(recipeListViewItems);
                    selectedRecipeListView.setItems(selectedRecipeListViewItems);
                }
                else{
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Error");
                    alert2.setHeaderText("Questa ricetta è stata assegnata.");
                    alert2.showAndWait();
                }

            }catch (UseCaseLogicException e){
                e.printStackTrace();
            }


        }



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
