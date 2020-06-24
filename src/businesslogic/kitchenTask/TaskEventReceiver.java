package businesslogic.kitchenTask;

public interface TaskEventReceiver {
    public void updateSheetAdded(SummarySheet s);

    public void updateRecipeAdded(Task t);

    public void updateTaskAssigned(Task t, Slot slot);
}
