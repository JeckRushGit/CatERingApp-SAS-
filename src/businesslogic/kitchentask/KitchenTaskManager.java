package businesslogic.kitchentask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuEventReceiver;
import businesslogic.recipe.KitchenDuty;
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
    private void notifyTaskAdded() {
        for (TaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAdded(this.currentSheet);
        }
    }


}
