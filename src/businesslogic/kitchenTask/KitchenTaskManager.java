package businesslogic.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.recipe.Recipe;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class KitchenTaskManager {
    private SummarySheet selectedSheet;
    private ArrayList<TaskEventReceiver> eventReceivers;

    public KitchenTaskManager(){
        this.eventReceivers = new ArrayList<>();
    }

    public SummarySheet chooseSheet(SummarySheet sheet) throws UseCaseLogicException, SummarySheetException {

        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        else if (!sheet.isCreator(user)){
            throw new SummarySheetException();
        }

        this.setSelectedSheet(sheet);

        return this.selectedSheet;
    }

    private void setSelectedSheet(SummarySheet selectedSheet) {
        this.selectedSheet = selectedSheet;
        selectedSheet.emptyList(selectedSheet.getId());
        selectedSheet.loadList();
    }

    public SummarySheet createSheet(Event event) throws UseCaseLogicException, SummarySheetException {

        User user = CatERing.getInstance().getUserManager().getCurrentUser();


        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        else if(event.isRefered()){
            throw new SummarySheetException();
        }


        SummarySheet s = new SummarySheet(event,user);

        this.setSelectedSheet(s);
        this.notifySheetAdded(s);

        return s;
    }

    public SummarySheet getSelectedSheet() {
        return selectedSheet;
    }

    public Task addRecipe(Recipe r) throws UseCaseLogicException, SummarySheetException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }

        Task t = selectedSheet.createTask(r);

        this.notifyRecipeAdded(t);

        return t;
    }


    public ObservableList<Slot> getShifts(){
        return ShiftBoard.loadAllShift();
    }

    public ArrayList<Task> getAvailableTask() {
        return selectedSheet.getList();
    }

    public Slot assignTask(Task t, Slot slot) throws UseCaseLogicException, SummarySheetException {
            User user = CatERing.getInstance().getUserManager().getCurrentUser();
            if (!user.isChef()) {
                throw new UseCaseLogicException();
            }

            if (selectedSheet == null || !selectedSheet.inList(t) || !(slot.getC()).availableShift(slot.getS())) {
                throw new SummarySheetException();
            }

            slot.setTask(t);
            slot.disavailable();

            this.notifyTaskAssigned(slot);

        return slot;
    }

    public ArrayList<Slot> assignTask(Task t, ArrayList<Slot> slots) throws UseCaseLogicException, SummarySheetException {
        ArrayList<Slot> changes = new ArrayList<>();
        for (Slot s: slots){
            changes.add(assignTask(t,s));
        }
        return changes;
    }

    public Slot setQuantityTask(Slot s, int q)throws UseCaseLogicException, SummarySheetException{
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        if (selectedSheet == null || !selectedSheet.inList(s.getT())) {
            throw new SummarySheetException();
        }
        s.getT().setQuantity(q);
        this.notifyTaskQuantityChange(s.getT());
        return s;
    }

    public Slot setTimeTask(Slot s, int t)throws UseCaseLogicException, SummarySheetException{
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        if (selectedSheet == null || !(selectedSheet.inList(s.getT()))) {
            throw new SummarySheetException();
        }
        s.getT().setTimeEstimate(t);
        this.notifyTaskTimeChange(s.getT());
        return s;
    }

    public Slot dischargeTask(Slot slot) throws UseCaseLogicException, SummarySheetException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }

        if (selectedSheet == null || slot.getT()==null || !(slot.getC()).availableShift(slot.getS())) {
            throw new SummarySheetException();
        }

        slot.removeTask();
        slot.setFree();

        this.notifyTaskdischarged(slot);

        return slot;
    }


    private void notifySheetAdded(SummarySheet s) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateSheetAdded(s);
        }
    }

    private void notifyRecipeAdded(Task t) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateRecipeAdded(t);
        }
    }

    private void notifyTaskAssigned(Slot slot) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateTaskAssigned(slot);
        }
    }

    private void notifyTaskQuantityChange(Task t){
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateTaskQuantity(t);
        }
    }

    private void notifyTaskTimeChange(Task t){
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateTaskTime(t);
        }
    }

    private void notifyTaskdischarged(Slot slot) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateTaskdischarged(slot);
        }
    }

    public ObservableList<SummarySheet> getAllSummarySheets() {
        return SummarySheet.loadAllSummarySheets();
    }

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }


}
