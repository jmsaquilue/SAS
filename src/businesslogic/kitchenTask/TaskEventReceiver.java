package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;

public interface TaskEventReceiver {
    public void updateSheetAdded(SummarySheet s);

    public void updateRecipeAdded(Task t);
    public void updateRecipeDeleted(Recipe r, SummarySheet sheet);

    public void updateTaskAssigned(Slot slot);

    public void updateTaskQuantity(Task t);
    public void updateTaskTime(Task t);

    public void updateTaskdischarged(Slot slot);


}
