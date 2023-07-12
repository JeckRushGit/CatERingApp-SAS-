package businesslogic.recipe;

import java.util.ArrayList;

public abstract class KitchenDuty {
    protected String name;
    protected int id;
    public int getId() {
        return id;
    }


    public abstract ArrayList<KitchenDuty> getSubDuties();


}
