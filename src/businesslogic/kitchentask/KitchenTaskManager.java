package businesslogic.kitchentask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuEventReceiver;
import businesslogic.recipe.KitchenDuty;
import businesslogic.shift.KitchenShift;
import businesslogic.shift.Shift;
import businesslogic.user.User;

import java.util.ArrayList;

public class KitchenTaskManager {

    public SummarySheet getCurrentSheet() {
        return currentSheet;
    }

    private SummarySheet currentSheet;

    private ArrayList<TaskEventReceiver> eventReceivers;

    public KitchenTaskManager() {
        eventReceivers = new ArrayList<>();
    }

    public void addEventReceiver(TaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public SummarySheet createSummarySheet(Event event, Service service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        if(event.getOrganizer() != user){
            throw new UseCaseLogicException();
        }
        if(!event.getServices().contains(service)){
            throw new UseCaseLogicException();
        }
        if(service.getServiceMenu() == null){
            throw new UseCaseLogicException();
        }
        if(service.getServiceMenu().getMenu() == null){
            throw new UseCaseLogicException();
        }

        SummarySheet newSumSheet = service.createSummarySheet();
        this.currentSheet = newSumSheet;
        this.notifySummarySheetCreated(service);

        return this.currentSheet;
    }



    public SummarySheet openSummarySheet(Event event,Service service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        if(event.getOrganizer() != user){
            throw new UseCaseLogicException();
        }
        if(!event.getServices().contains(service)){
            throw new UseCaseLogicException();
        }
        if(service.getSummarySheet() == null){
            throw new UseCaseLogicException();
        }
        this.currentSheet = service.getSummarySheet();
        return this.currentSheet;
    }

    public void deleteSummarySheet(Event event,Service service) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        if(event.getOrganizer() != user){
            throw new UseCaseLogicException();
        }
        if(!event.getServices().contains(service)){
            throw new UseCaseLogicException();
        }
        if(service.getSummarySheet() == null){
            throw new UseCaseLogicException();
        }

        notifySummarySheetDeleted(service.getSummarySheet());
        service.removeSummarySheet();
    }

    public void addTask(KitchenDuty kitchenDuty) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null){
            throw new UseCaseLogicException();
        }
        currentSheet.addTask(kitchenDuty);
        notifyTaskAdded();
    }

    public void deleteTask(KitchenTask kitchenTask) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null){
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.getTasks().contains(kitchenTask)){
            throw new UseCaseLogicException();
        }

        notifyTaskDeleted(kitchenTask);
        currentSheet.removeTask(kitchenTask);
    }

    public void orderTask(KitchenTask task,int newPosition) throws UseCaseLogicException, SummarySheetException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()){
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null){
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.getTasks().contains(task)){
            throw new UseCaseLogicException();
        }
        if(newPosition < 0 || newPosition >= this.currentSheet.getTasks().size()){
            throw new SummarySheetException();
        }
        notifyOrderTask(task.getPosition(),newPosition);
        currentSheet.orderTask(task,newPosition);

    }

    public ArrayList<KitchenShift> getShiftTable(){
      return CatERing.getInstance().getShiftManager().getShiftTable();
    }

    public void fillTask(KitchenTask task, KitchenShift shift, Double quantity, Integer portions, String timeEstimate) throws UseCaseLogicException, TaskException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }
        if(shift.isFull()) {
            throw new TaskException();
        }

        currentSheet.fillTask(task, shift, quantity, portions, timeEstimate);
        notifyTaskFilled(task);
    }

    public void moveTask(KitchenTask task, KitchenShift shift) throws UseCaseLogicException, TaskException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }
        if(task.getShift() == null) {
            throw new UseCaseLogicException();
        }
        if(shift.isFull()) {
            throw new TaskException();
        }

        task.changeShift(shift);
        notifyTaskMoved(task);
    }

    public void removeTaskShift(KitchenTask task) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }
        if(task.getShift() == null) {
            throw new UseCaseLogicException();
        }

        currentSheet.removeTaskShift(task);
        notifyTaskShiftRemoved(task);
    }

    public void changeQuantity(KitchenTask task, double quantity) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }

        task.setQuantity(quantity);
        notifyTaskQuantityChanged(task);
    }

    public void changePortions(KitchenTask task, int portions) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }

        task.setPortions(portions);
        notifyTaskPortionsChanged(task);
    }

    public void changeTimeEstimate(KitchenTask task, String timeEstimate) throws UseCaseLogicException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }

        task.setTimeEstimate(timeEstimate);
        notifyTaskTimeEstimateChanged(task);
    }

    public void assignCook(KitchenTask task, User cook) throws UseCaseLogicException, TaskException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }
        if(task.getShift() == null) {
            throw new UseCaseLogicException();
        }
        if(!cook.isKitchenAvailable(task.getShift())) {
            throw new TaskException();
        }
        if(task.hasCook()) {
            throw new TaskException();
        }

        task.assignCook(cook);
        notifyTaskCookAssigned(task);
    }

    public void removeCook(KitchenTask task) throws UseCaseLogicException, TaskException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }
        if(task.getShift() == null) {
            throw new UseCaseLogicException();
        }
        if(!(task.hasCook())) {
            throw new TaskException();
        }

        notifyTaskCookRemoved(task);
        task.removeCook();
    }

    public void changeCook(KitchenTask task, User cook) throws UseCaseLogicException, TaskException {
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()) {
            throw new UseCaseLogicException();
        }
        if(this.currentSheet == null) {
            throw new UseCaseLogicException();
        }
        if(!this.currentSheet.hasTask(task)) {
            throw new UseCaseLogicException();
        }
        if(task.getShift() == null) {
            throw new UseCaseLogicException();
        }
        if(!cook.isKitchenAvailable(task.getShift())) {
            throw new TaskException();
        }
        if(!(task.hasCook())) {
            throw new TaskException();
        }
        removeCook(task);
        assignCook(task, cook);
    }

    private void notifyTaskCookRemoved(KitchenTask task) {
        for(TaskEventReceiver receiver: this.eventReceivers) {
            receiver.updateTaskCookRemoved(this.currentSheet, task);
        }
    }

    private void notifyTaskCookAssigned(KitchenTask task) {
        for(TaskEventReceiver receiver: this.eventReceivers) {
            receiver.updateTaskCookAssigned(this.currentSheet, task);
        }
    }

    private void notifyTaskTimeEstimateChanged(KitchenTask task) {
        for(TaskEventReceiver receiver: this.eventReceivers) {
            receiver.updateTaskTimeEstimateChanged(this.currentSheet, task);
        }
    }

    private void notifyTaskPortionsChanged(KitchenTask task) {
        for(TaskEventReceiver receiver: this.eventReceivers) {
            receiver.updateTaskPortionsChanged(this.currentSheet, task);
        }
    }

    private void notifyTaskQuantityChanged(KitchenTask task) {
        for(TaskEventReceiver receiver: this.eventReceivers) {
            receiver.updateTaskQuantityChanged(this.currentSheet, task);
        }
    }

    private void notifyTaskShiftRemoved(KitchenTask task) {
        for(TaskEventReceiver receiver: this.eventReceivers) {
            receiver.updateTaskShiftRemoved(this.currentSheet, task);
        }
    }

    private void notifyTaskMoved(KitchenTask task) {
        for(TaskEventReceiver receiver : this.eventReceivers) {
            receiver.updateTaskMoved(this.currentSheet, task);
        }
    }

    private void notifyTaskFilled(KitchenTask task) {
        for(TaskEventReceiver receiver : this.eventReceivers) {
            receiver.updateTaskFilled(this.currentSheet, task);
        }
    }

    private void notifyOrderTask(int oldPosition, int newPosition) {
        for(TaskEventReceiver receiver : this.eventReceivers){
            receiver.updateOrderTask(this.currentSheet,oldPosition,newPosition);
        }
    }

    private void notifyTaskDeleted(KitchenTask kitchenTask) {
        for(TaskEventReceiver receiver : this.eventReceivers){
            receiver.updateTaskDeleted(this.currentSheet,kitchenTask);
        }
    }

    private void notifySummarySheetCreated(Service service) {
        for(TaskEventReceiver receiver : this.eventReceivers){
            receiver.updateSummarySheetCreated(service,this.currentSheet);
        }
    }

    private void notifySummarySheetDeleted(SummarySheet summarySheet) {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateSummarySheetDeleted(summarySheet);
        }
    }

    public void setCurrentSheet(SummarySheet currentSheet) {
        this.currentSheet = currentSheet;
    }

    private void notifyTaskAdded() {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAdded(this.currentSheet);
        }
    }


}
