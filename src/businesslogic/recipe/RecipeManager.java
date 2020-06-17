package businesslogic.recipe;

import businesslogic.UseCaseLogicException;
import businesslogic.kitchenTask.SummarySheet;
import javafx.collections.ObservableList;

public class RecipeManager {
    private Recipe currentRecipe;
    public void chooseRecipe(Recipe r)throws UseCaseLogicException, RecipeException {
        currentRecipe=r;
    }

    public Recipe getCurrentRecipe(){
        return currentRecipe;
    }

    public ObservableList<Recipe> getAllAvailableRecipes(SummarySheet sheet){
        return currentRecipe.loadAvailableRecipes(sheet);
    }

    public ObservableList<Recipe> getAllSelectedRecipes(SummarySheet sheet) {
        return currentRecipe.loadSelectedRecipes(sheet);
    }
}
