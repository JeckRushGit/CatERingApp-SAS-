package businesslogic.kitchentask;

import businesslogic.event.Service;
import businesslogic.recipe.KitchenDuty;
import persistence.PersistenceManager;

import java.util.ArrayList;

public class SummarySheet {
    private int id;
    private ArrayList<KitchenTask> tasks;

    public SummarySheet(ArrayList<KitchenDuty> kitchenDuties){
        ArrayList<KitchenTask> kitchenTasks = new ArrayList<>();
        for (KitchenDuty kitchenDuty: kitchenDuties){
            KitchenTask kitchenTask = new KitchenTask(kitchenDuty);
            kitchenTasks.add(kitchenTask);
        }
        this.tasks = kitchenTasks;
    }

    public static void saveNewSummarySheet(SummarySheet summarySheet, Service service){
        String summarySheetInsert = "INSERT into catering.summarysheets (service_id) VALUES ("+service.getId()+")";

        int res = PersistenceManager.executeUpdate(summarySheetInsert);
        summarySheet.id = PersistenceManager.getLastId();
        if(res > 0){
            if(summarySheet.tasks.size() > 0){
                KitchenTask.saveAllKitchenTasks(summarySheet.id,summarySheet.tasks);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
