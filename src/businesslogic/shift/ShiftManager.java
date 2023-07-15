package businesslogic.shift;

import java.util.ArrayList;

public class ShiftManager {
    public ArrayList<KitchenShift> getShiftTable(){
        ArrayList<KitchenShift> shiftTable =  KitchenShift.loadAllKitchenShifts();
        return shiftTable;
    }
}
