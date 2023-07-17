import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.kitchentask.KitchenTask;
import businesslogic.kitchentask.SummarySheet;
import businesslogic.kitchentask.SummarySheetException;
import businesslogic.kitchentask.TaskException;
import businesslogic.menu.Menu;
import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Recipe;
import businesslogic.recipe.SubDuty;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class KitchenTaskTest {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());


            List<Event> list = CatERing.getInstance().getEventManager().getEventInfo();

            Event targetEvent = list.get(1);
            Service service = targetEvent.getServices().get(0);




            System.out.println("TEST CREATE SUMMARY SHEET");
//            CatERing.getInstance().getKitchenTaskManager().deleteSummarySheet(targetEvent,service);
            SummarySheet sheet = CatERing.getInstance().getKitchenTaskManager().createSummarySheet(targetEvent, service);

            System.out.println("TEST ADD KITCHENTASK");
            ObservableList<Recipe> recipes =  CatERing.getInstance().getRecipeManager().getRecipes();
            Recipe r1 = recipes.get(5);
            Recipe r2 = recipes.get(7);
            KitchenDuty s1 = r2.getSubDuties().get(0);
            CatERing.getInstance().getKitchenTaskManager().addTask(r1);
            CatERing.getInstance().getKitchenTaskManager().addTask(s1);

            System.out.println("TEST ORDER TASKS");
            KitchenTask taskToMove = sheet.getTasks().get(2);
            CatERing.getInstance().getKitchenTaskManager().orderTask(taskToMove,0);
            KitchenTask taskToMove1 = sheet.getTasks().get(0);
            CatERing.getInstance().getKitchenTaskManager().orderTask(taskToMove1,2);

            System.out.println("TEST EXAMIN SHIFT TABLE");
            ArrayList<KitchenShift> table = CatERing.getInstance().getKitchenTaskManager().getShiftTable();

            System.out.println("TEST FILL TASK");
            KitchenTask taskToFill = sheet.getTasks().get(0);
            ArrayList<KitchenShift> kitchenShifts = KitchenShift.loadAllKitchenShifts();
            KitchenShift kitchenShift = kitchenShifts.get(0);
            CatERing.getInstance().getKitchenTaskManager().fillTask(taskToFill,kitchenShift,3.0,2,"50 minuti");

            System.out.println("TEST ASSIGN COOK");
            KitchenTask taskToAssignCook = sheet.getTasks().get(0);
            User user = User.loadUser("Marinella");
            CatERing.getInstance().getKitchenTaskManager().assignCook(taskToAssignCook,user);

        } catch (UseCaseLogicException | SummarySheetException | TaskException e) {
            System.out.println("Errore di logica nello use case");
        }


    }
}
