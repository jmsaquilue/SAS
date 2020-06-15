package businesslogic.recipe;

import businesslogic.UseCaseLogicException;
import javafx.collections.ObservableList;

public class RecipeManager {
    private Recipe currentRecipe;
    public void chooseRecipe(Recipe r)throws UseCaseLogicException, RecipeException {
        currentRecipe=r;
    }

    public Recipe getCurrentRecipe(){
        return currentRecipe;
    }

    public ObservableList<Recipe> getAllRecipes(){
        return currentRecipe.loadRecipes();
    }
}
