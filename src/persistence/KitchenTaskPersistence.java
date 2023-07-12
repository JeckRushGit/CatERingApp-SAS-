package persistence;

import businesslogic.event.Event;
import businesslogic.event.Service;
import businesslogic.kitchentask.SummarySheet;
import businesslogic.kitchentask.TaskEventReceiver;

public class KitchenTaskPersistence implements TaskEventReceiver {
    @Override
    public void updateSummarySheetCreated(Service service, SummarySheet currentSheet) {
        SummarySheet.saveNewSummarySheet(currentSheet,service);
    }
}
