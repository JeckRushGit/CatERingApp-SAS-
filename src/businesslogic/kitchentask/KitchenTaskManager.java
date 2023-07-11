package businesslogic.kitchentask;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.user.User;

public class KitchenTaskManager {

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


        return null;
    }
}
