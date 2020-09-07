package businesslogic.kitchenTask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.recipe.Recipe;
import businesslogic.user.Cook;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Map;

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
        else if(selectedSheet == null || selectedSheet.exits(r)){
            throw new SummarySheetException();
        }

        Task t = selectedSheet.createTask(r);

        this.notifyRecipeAdded(t);

        return t;
    }

    public boolean deleteRecipe(Recipe r) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        boolean inUse = r.inUse(selectedSheet);
        if (!inUse){
            selectedSheet.deleteTask(r);
            this.notifyRecipeDeleted(r);
        }

        return !inUse;
    }

    public void move(Recipe r, int pos) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();

        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        boolean inUse = r.inUse(selectedSheet);
        if (!inUse){
            selectedSheet.move(r,pos);
            this.notifyMovedRecipes(selectedSheet);
        }
    }



    public ObservableList<Slot> getShifts(){
        return ShiftBoard.loadAllShift(selectedSheet);
    }

    public ArrayList<Task> getAvailableTask(ArrayList<Shift> shifts) {
        Map<Integer,Task> all = selectedSheet.getTasks();
        for (Shift s: shifts){
            all = s.getAvailableTask(all);
        }
        return new ArrayList<Task> (all.values());
    }

    public Slot assignTask(Task t, Slot slot) throws UseCaseLogicException, SummarySheetException {
        return assignTask(t,slot,null);


    }

    public Slot assignTask(Task t, Slot slot, Cook c) throws UseCaseLogicException, SummarySheetException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if (!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if (selectedSheet == null || !selectedSheet.inList(t) || (slot.getC() != null && !(slot.getC()).availableShift(slot.getS()))
                || (c != null && !(c.availableShift(slot.getS())))) {
            throw new SummarySheetException();
        }

        if (!slot.getAvailable())
            this.notifyTaskdischarged(slot,slot.getT());

        slot.setCook(c);
        if (c != null) {

            slot.getS().removeCook(c);
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
        if (selectedSheet == null || slot.getT()==null || (slot.getC() != null && (slot.getC()).availableShift(slot.getS()))){
            throw new SummarySheetException();
        }

        Task old = slot.getT();

        slot.removeTask();
        slot.setFree();

        if (slot.getC() != null)
            slot.getS().addCook(slot.getC());

        this.notifyTaskdischarged(slot,old);

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

    private void notifyRecipeDeleted(Recipe r){
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateRecipeDeleted(r,selectedSheet);
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

    private void notifyTaskdischarged(Slot slot, Task task) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateTaskdischarged(slot,task);
        }
    }

    private void notifyMovedRecipes(SummarySheet sheet) {
        for (TaskEventReceiver er: this.eventReceivers){
            er.updateMovedTasks(sheet);
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
