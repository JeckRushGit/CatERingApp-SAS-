import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.event.ServiceMenu;
import businesslogic.kitchentask.KitchenTaskManager;
import businesslogic.menu.Menu;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class KitchenTaskTest1 {
    public static void main(String[] args) {

        System.out.println("TEST FAKE LOGIN");
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");
        System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());


        List<Event> list = CatERing.getInstance().getEventManager().getEventInfo();

        Event e = list.get(0);
        Service s = e.getServices().get(0);
        Event targetEvent = null;

        ObservableList<Event> events = CatERing.getInstance().getEventManager().getEventInfo();
        for(Event ev: events){
            for(Service se: ev.getServices()){
                if(se.getServiceMenu() != null){
                    s = se;
                    targetEvent = ev;
                    break;
                }
            }
        }

        try {
            CatERing.getInstance().getKitchenTaskManager().createSummarySheet(targetEvent,s);
        } catch (UseCaseLogicException ex) {
            ex.printStackTrace();
        }

    }
}
