import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.kitchentask.KitchenTask;
import businesslogic.kitchentask.SummarySheet;

import java.util.List;

public class KitchenTaskTest5d {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            List<Event> list = CatERing.getInstance().getEventManager().getEventInfo();


            Event targetEvent = list.get(1);
            Service service = targetEvent.getServices().get(0);

            System.out.println("TEST OPEN SUMMARY SHEET");
            SummarySheet sheet = CatERing.getInstance().getKitchenTaskManager().openSummarySheet(targetEvent, service);

            System.out.println("CHANGE TASK PORTIONS");
            KitchenTask targetTask = sheet.getTasks().get(0);
            CatERing.getInstance().getKitchenTaskManager().changePortions(targetTask, 20);


        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }

    }
}
