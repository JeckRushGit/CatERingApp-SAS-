import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.kitchentask.KitchenTask;
import businesslogic.kitchentask.SummarySheet;
import businesslogic.kitchentask.SummarySheetException;
import businesslogic.kitchentask.TaskException;
import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Recipe;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class KitchenTaskTest1a {
    public static void main(String[] args) {
        try {
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            List<Event> list = CatERing.getInstance().getEventManager().getEventInfo();

            Event targetEvent = list.get(1);
            Service service = targetEvent.getServices().get(0);

            System.out.println("TEST OPEN SUMMARY SHEET");
//            CatERing.getInstance().getKitchenTaskManager().deleteSummarySheet(targetEvent,service);
            SummarySheet sheet = CatERing.getInstance().getKitchenTaskManager().openSummarySheet(targetEvent, service);


            System.out.println("TEST ADD KITCHENTASK");
            ObservableList<Recipe> recipes =  CatERing.getInstance().getRecipeManager().getRecipes();
            Recipe r1 = Recipe.loadRecipeById(10);
            CatERing.getInstance().getKitchenTaskManager().addTask(r1);


            System.out.println("TEST ORDER TASKS");
            KitchenTask taskToMove = sheet.getTasks().get(2);
            CatERing.getInstance().getKitchenTaskManager().orderTask(taskToMove,0);


            System.out.println("TEST EXAMIN SHIFT TABLE");
            ArrayList<KitchenShift> table = CatERing.getInstance().getKitchenTaskManager().getShiftTable();

            System.out.println("TEST FILL TASK");
            KitchenTask taskToFill = sheet.getTasks().get(1);
            ArrayList<KitchenShift> kitchenShifts = KitchenShift.loadAllKitchenShifts();
            KitchenShift kitchenShift = kitchenShifts.get(1);
            CatERing.getInstance().getKitchenTaskManager().fillTask(taskToFill,kitchenShift,3.0,2,"50 minuti");

            System.out.println("TEST ASSIGN COOK");
            KitchenTask taskToAssignCook = sheet.getTasks().get(1);
            User user = User.loadUser("Guido");
            CatERing.getInstance().getKitchenTaskManager().assignCook(taskToAssignCook,user);

        }catch (UseCaseLogicException | TaskException | SummarySheetException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
