import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;

import java.util.List;

public class KitchenTaskTest1b {
    public static void main(String[] args) {
        try {
        System.out.println("TEST FAKE LOGIN");
        CatERing.getInstance().getUserManager().fakeLogin("Lidia");
        System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());


        List<Event> list = CatERing.getInstance().getEventManager().getEventInfo();

        Event targetEvent = list.get(1);
        Service service = targetEvent.getServices().get(0);




        System.out.println("TEST DELETE SUMMARY SHEET");
            CatERing.getInstance().getKitchenTaskManager().deleteSummarySheet(targetEvent,service);
        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
