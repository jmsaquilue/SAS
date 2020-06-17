package businesslogic.recipe;

import businesslogic.kitchenTask.SummarySheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private static Map<Integer, Recipe> recipesLoaded = FXCollections.observableHashMap();



    // Constructor per le prove
    public Recipe(int i, String name, String description){
        this.id=i;
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return id;
    }
    public static ObservableList<Recipe> loadAvailableRecipes(SummarySheet sheet){
        String query = "SELECT * FROM Recipes WHERE id not in (SELECT recipeid FROM Tasks WHERE summaryid='"+sheet.getId()+"');";
        ArrayList<Recipe> recipes = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                    String n=rs.getString("name");
                    String d=rs.getString("description");
                    Recipe r = new Recipe(id, n, d);
                    recipes.add(r);
            }
        });


        for (Recipe s: recipes) {
            recipesLoaded.put(s.id, s);
        }
        return FXCollections.observableArrayList(recipesLoaded.values());
    }

    public static ObservableList<Recipe> loadSelectedRecipes(SummarySheet sheet){
        String query = "SELECT * FROM Recipes WHERE id in (SELECT recipeid FROM Tasks WHERE summaryid='"+sheet.getId()+"');";
        ArrayList<Recipe> recipes = new ArrayList<>();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int id = rs.getInt("id");
                String n=rs.getString("name");
                String d=rs.getString("description");
                Recipe r = new Recipe(id, n, d);
                recipes.add(r);
            }
        });


        return FXCollections.observableArrayList(recipes);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
