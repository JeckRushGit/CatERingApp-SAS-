package businesslogic.kitchentask;

import businesslogic.event.Event;
import businesslogic.event.Service;

public interface TaskEventReceiver {
    void updateSummarySheetCreated(Service service,SummarySheet currentSheet);
}
