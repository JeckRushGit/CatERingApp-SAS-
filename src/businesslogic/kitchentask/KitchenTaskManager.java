package businesslogic.kitchentask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.menu.MenuEventReceiver;
import businesslogic.user.User;

import java.util.ArrayList;

public class KitchenTaskManager {

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

    private void notifySummarySheetCreated(Service service) {
        for(TaskEventReceiver receiver : this.eventReceivers){
            receiver.updateSummarySheetCreated(service,this.currentSheet);
        }
    }
}
