package businesslogic.event;

import businesslogic.kitchentask.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Objects;

public class Service implements EventItemInfo {
    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;

    private SummarySheet summarySheet;

    private ServiceMenu serviceMenu;

    public Service(String name) {
        this.name = name;
    }

    public SummarySheet getSummarySheet() {
        return summarySheet;
    }

    public ServiceMenu getServiceMenu() {
        return serviceMenu;
    }

    public void setServiceMenu(ServiceMenu serviceMenu) {
        this.serviceMenu = serviceMenu;
    }

    public String toString() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp." + " servicemenu : "+serviceMenu;
    }


    public SummarySheet createSummarySheet(){
        Menu menu = this.serviceMenu.getMenu();
        ArrayList<Recipe> recipes = menu.getNeededRecipes();
        ArrayList<KitchenDuty> kitchenDuties = new ArrayList<>();
        for(Recipe recipe : recipes){
            for(KitchenDuty kitchenDuty : recipe.getSubDuties()){
                kitchenDuties.add(kitchenDuty);
            }
        }
        SummarySheet newSummarySheet = new SummarySheet(kitchenDuties);
        this.summarySheet = newSummarySheet;
        return this.summarySheet;
    }

    public int getId() {
        return id;
    }


    // STATIC METHODS FOR PERSISTENCE




    public static ObservableList<Service> loadServiceInfoForEvent(int event_id) {
        ObservableList<Service> result = FXCollections.observableArrayList();
        String query = "SELECT id, name, service_date, time_start, time_end, expected_participants " +
                "FROM services WHERE event_id = " + event_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                String s = rs.getString("name");
                Service serv = new Service(s);
                serv.id = rs.getInt("id");
                serv.date = rs.getDate("service_date");
                serv.timeStart = rs.getTime("time_start");
                serv.timeEnd = rs.getTime("time_end");
                serv.participants = rs.getInt("expected_participants");
                serv.serviceMenu = ServiceMenu.loadServiceMenuInfoForService(serv.id);
                serv.summarySheet = SummarySheet.loadSummarySheetInfoForService(serv.id);
                result.add(serv);
            }
        });

        return result;
    }


    public void removeSummarySheet() {
        this.summarySheet.deleteTasks();
        this.summarySheet = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return id == service.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
