package businesslogic.kitchentask;

import businesslogic.event.Event;
import businesslogic.event.Service;

public interface TaskEventReceiver {
    void updateSummarySheetCreated(Service service,SummarySheet currentSheet);

    void updateSummarySheetDeleted(SummarySheet summarySheet);

    void updateTaskAdded(SummarySheet currentSheet);

    void updateTaskDeleted(SummarySheet summarySheet,KitchenTask kitchenTask);

    void updateOrderTask(SummarySheet currentSheet, int oldPosition, int newPosition);
}
