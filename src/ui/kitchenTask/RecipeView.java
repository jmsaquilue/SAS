package ui.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchenTask.SummarySheetException;
import businesslogic.kitchenTask.Task;
import businesslogic.recipe.Recipe;
import businesslogic.recipe.RecipeException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;

public class RecipeView {
    private RecipeManager recipemanagercontroller;
    ObservableList<Recipe> free;
    @FXML
    GridPane pn;
    @FXML
    ListView<Recipe> recipefree;
    @FXML
    ListView<Recipe> recipeadd;

    public void addbuttom(){
        try {
            Recipe r = recipefree.getSelectionModel().getSelectedItem();
            CatERing.getInstance().getRecipeManager().chooseRecipe(r);
            try {
                Task s = CatERing.getInstance().getKitchenTaskManager().getSelectedSheet().createTask(r);
            } catch (UseCaseLogicException | SummarySheetException ex) {
                ex.printStackTrace();
            }
            // TODO: CU 2
        } catch (UseCaseLogicException | RecipeException ex){
            ex.printStackTrace();
        }
    }

    public void initialize() {
        if (free == null) {
            // TODO: Mucho que hacer.
            free = CatERing.getInstance().getRecipeManager().getAllRecipes();
            recipefree.setItems(free);
            recipefree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            recipeadd.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        }
    }


    public void setParent(RecipeManager rp) {
        recipemanagercontroller = rp;
    }
}
