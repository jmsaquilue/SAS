package businesslogic.kitchenTask;

public interface TaskEventReceiver {
    public void updateSheetAdded(SummarySheet s);

    public void updateRecipeAdded(Task t);

    public void updateTaskAssigned(Slot slot);

    public void updateTaskQuantity(Task t);
    public void updateTaskTime(Task t);

}
