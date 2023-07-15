import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.kitchentask.KitchenTask;
import businesslogic.kitchentask.TaskException;
import businesslogic.recipe.SubDuty;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;

import java.sql.SQLOutput;
import java.util.List;

public class KitchenTaskTest5 {
    public static void main(String[] args) {
        System.out.println("TEST FAKE LOGIN");
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");
        System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

        List<Event> events = Event.loadAllEventInfo();
        Event ev = null;
        Service serv = null;

        for(Event e : events) {
            if(e.getId() == 2) {
                ev = e;
                System.out.println(ev.getId());
                System.out.println(ev.getServices());
                System.out.println(ev.getOrganizer());
            }
        }

        List<Service> services = Service.loadServiceInfoForEvent(ev.getId());
        System.out.println(services);
        for(Service s : services) {
            if(s.getId() == 1) {
                serv = s;
                System.out.println(serv.getId());
            }
        }

        try {
            CatERing.getInstance().getKitchenTaskManager().openSummarySheet(ev, serv);
            System.out.println(serv.getSummarySheet().getTasks().get(0));
            CatERing.getInstance().getKitchenTaskManager().changeCook(serv.getSummarySheet().getTasks().get(0), User.loadUserById(4));
        } catch (UseCaseLogicException | TaskException e) {
            throw new RuntimeException(e);
        }


    }
}
