package businesslogic.kitchentask;

import businesslogic.event.Event;
import businesslogic.event.Service;

public interface TaskEventReceiver {
    void updateSummarySheetCreated(Service service,SummarySheet currentSheet);

    void updateSummarySheetDeleted(SummarySheet summarySheet);

    void updateTaskAdded(SummarySheet currentSheet);

    void updateTaskDeleted(SummarySheet summarySheet,KitchenTask kitchenTask);

    void updateOrderTask(SummarySheet currentSheet, int oldPosition, int newPosition);

    void updateTaskFilled(SummarySheet currentSheet, KitchenTask task);

    void updateTaskMoved(SummarySheet currentSheet, KitchenTask task);

    void updateTaskShiftRemoved(SummarySheet currentSheet, KitchenTask task);

    void updateTaskQuantityChanged(SummarySheet currentSheet, KitchenTask task);

    void updateTaskPortionsChanged(SummarySheet currentSheet, KitchenTask task);

    void updateTaskTimeEstimateChanged(SummarySheet currentSheet, KitchenTask task);

    void updateTaskCookAssigned(SummarySheet currentSheet, KitchenTask task);

    void updateTaskCookRemoved(SummarySheet currentSheet, KitchenTask task);
}
