package businesslogic.recipe;

import businesslogic.event.ServiceMenu;
import businesslogic.menu.Menu;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubDuty extends KitchenDuty{

    @Override
    public ArrayList<KitchenDuty> getSubDuties() {
        return null;
    }

    public SubDuty(String name) {
        id = 0;
        this.name = name;
    }

    public static ArrayList<KitchenDuty> loadSubDutiesForRecipe(int recipe_id){
        ArrayList<KitchenDuty> subDuties = new ArrayList<>();
        String query = "SELECT *" +
                "FROM subduties WHERE recipe_id = " + recipe_id;

        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                SubDuty subDuty = new SubDuty(rs.getString("name"));
                subDuty.id = rs.getInt("id");
                subDuties.add(subDuty);
            }
        });

        return subDuties;
    }

    @Override
    public String toString() {
        return name;
    }
}
