package persistence;

import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.kitchentask.KitchenTask;
import businesslogic.kitchentask.SummarySheet;
import businesslogic.kitchentask.TaskEventReceiver;

import java.util.ArrayList;

public class KitchenTaskPersistence implements TaskEventReceiver {
    @Override
    public void updateSummarySheetCreated(Service service, SummarySheet currentSheet) {
        SummarySheet.saveNewSummarySheet(currentSheet,service);
    }

    @Override
    public void updateSummarySheetDeleted(SummarySheet summarySheet) {
        SummarySheet.deleteSummarySheet(summarySheet);
    }

    @Override
    public void updateTaskAdded(SummarySheet currentSheet) {
        ArrayList<KitchenTask> tasks = currentSheet.getTasks();
        for(KitchenTask task : tasks){
            if(task.getId() == 0){
                KitchenTask.saveNewKitchenTask(task,currentSheet.getId(),task.getKitchenDuty().getId());
            }
        }
    }

    @Override
    public void updateTaskDeleted(SummarySheet summarySheet,KitchenTask kitchenTask) {
        KitchenTask.deleteKitchenTask(summarySheet,kitchenTask);
        SummarySheet.shiftPositions(summarySheet,kitchenTask.getPosition());
    }

    @Override
    public void updateOrderTask(SummarySheet currentSheet, int oldPosition, int newPosition) {
        SummarySheet.orderTasks(currentSheet,oldPosition,newPosition);
    }


}
