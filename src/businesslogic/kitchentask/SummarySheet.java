package businesslogic.kitchentask;

import businesslogic.event.Service;
import businesslogic.event.ServiceMenu;
import businesslogic.menu.Menu;
import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.SubDuty;
import businesslogic.shift.KitchenShift;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class SummarySheet {
    private int id;
    private ArrayList<KitchenTask> tasks;


    public ArrayList<KitchenTask> getTasks() {
        return tasks;
    }

    public SummarySheet(ArrayList<KitchenDuty> kitchenDuties){
        ArrayList<KitchenTask> kitchenTasks = new ArrayList<>();
        int position = 0;
        for (KitchenDuty kitchenDuty: kitchenDuties){
            KitchenTask kitchenTask = new KitchenTask(kitchenDuty,position);
            kitchenTasks.add(kitchenTask);
            position++;
        }
        this.tasks = kitchenTasks;
    }

    private SummarySheet(){
        this.id = 0;
        this.tasks = new ArrayList<>();
    }

    public void addTask(KitchenDuty kitchenDuty) {
        ArrayList<KitchenDuty> subDuties = kitchenDuty.getSubDuties();
        if(subDuties == null){
            KitchenTask newKitchenTask = new KitchenTask(kitchenDuty,tasks.size());
            this.tasks.add(newKitchenTask);
        }else{
            for(KitchenDuty subDuty: subDuties){
                KitchenTask newKitchenTask = new KitchenTask(subDuty,tasks.size());
                this.tasks.add(newKitchenTask);
            }
        }
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

    public static SummarySheet loadSummarySheetInfoForService(int service_id) {
        String query = "SELECT *" +
                "FROM summarysheets WHERE service_id = " + service_id;

        SummarySheet summarySheet = new SummarySheet();

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int summarySheetId = rs.getInt("id");
                summarySheet.id = summarySheetId;

            }
        });


        if(summarySheet.id == 0){
            return null;
        }else{
            ArrayList<KitchenTask> tasks = KitchenTask.getTasksForSummarySheet(summarySheet.id);
            summarySheet.tasks = tasks;
        }
        return summarySheet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void removeTask(KitchenTask kitchenTask) {
        if(kitchenTask.hasCook()){
            kitchenTask.removeCook();
        }
        this.tasks.remove(kitchenTask);
    }

    public static void shiftPositions(SummarySheet summarySheet,int position) {
        String update = "UPDATE kitchentasks SET position = position - 1 WHERE " +
                "summarysheet_id = "+summarySheet.getId()+" AND position > "+position+"";
        PersistenceManager.executeUpdate(update);
        for(KitchenTask kitchenTask : summarySheet.tasks){
            if(kitchenTask.getPosition() > position){
                kitchenTask.setPosition(kitchenTask.getPosition() - 1);
            }
        }
    }

    public void orderTask(KitchenTask task, int newPosition) {
        int oldPosition = task.getPosition();
        task.setPosition(newPosition);
        KitchenTask toSwap = tasks.get(newPosition);
        toSwap.setPosition(oldPosition);
        Collections.swap(tasks,oldPosition,newPosition);
    }

    public static void deleteSummarySheet(SummarySheet summarySheet) {
        for(KitchenTask kitchenTask : summarySheet.tasks){
            KitchenTask.deleteKitchenTask(kitchenTask);
        }
        String delSummarySheet = "DELETE FROM summarysheets WHERE id = " + summarySheet.id;
        PersistenceManager.executeUpdate(delSummarySheet);
    }

    public static void orderTasks(SummarySheet currentSheet, int oldPosition, int newPosition) {

        String update =
                "UPDATE kitchentasks\n" +
                "SET position = CASE\n" +
                "    WHEN position = "+oldPosition+" THEN "+newPosition+"\n" +
                "    WHEN position = "+newPosition+" THEN "+oldPosition+"\n" +
                "    ELSE position\n" +
                "  END\n" +
                "WHERE summarysheet_id = "+currentSheet.id+"\n" +
                "AND (position = "+oldPosition+" OR position = "+newPosition+");";
        PersistenceManager.executeUpdate(update);
    }

    public boolean hasTask(KitchenTask task) {
        return tasks.contains(task);
    }

    public void fillTask(KitchenTask task, KitchenShift shift, Double quantity, Integer portions, String timeEstimate) {
        task.setShift(shift);

        if(quantity != null) {
            task.setQuantity(quantity);
        }

        if(portions != null) {
            task.setPortions(portions);
        }

        if(timeEstimate != null) {
            task.setTimeEstimate(timeEstimate);
        }
    }

    public void removeTaskShift(KitchenTask task) {
        if(task.hasCook()) {
            task.removeCook();
        }
        task.removeShift();
    }

    public void deleteTasks() {
        for(int i = 0 ; i < tasks.size() ; i++){
            this.removeTask(tasks.get(i));
        }
//        for(KitchenTask task: this.tasks){
//            this.removeTask(task);
//        }
    }
}

