package businesslogic.event;

import businesslogic.menu.Menu;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceMenu {
    private Menu menu;

    private int id;
    private boolean approved;
    private String proposals;

    public ServiceMenu(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu(){
        return this.menu;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getProposals() {
        return proposals;
    }

    public void setProposals(String proposals) {
        this.proposals = proposals;
    }

    public static ServiceMenu loadServiceMenuInfoForService(int service_id){
        List<Menu> menuList = Menu.loadAllMenus();
        List<ServiceMenu> serviceMenus = new ArrayList<>();
        String query = "SELECT *" +
                "FROM servicemenu WHERE service_id = " + service_id;
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                int menu_id = rs.getInt("menu_id");
                Menu menu = null;
                for(Menu m : menuList){
                    if(m.getId() == menu_id){
                        menu = m;
                    }
                }
                ServiceMenu sm = new ServiceMenu(menu);
                sm.approved = (rs.getBoolean("approved"));
                sm.proposals = (rs.getString("proposals"));
                sm.id = rs.getInt("id");
                serviceMenus.add(sm);
            }
        });
        if(serviceMenus.size() > 0)
            return serviceMenus.get(0);
        return null;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return
                "menu= " + menu +
                ", id= " + id +
                ", approved= " + approved +
                ", proposals= '" + proposals + '\'';
    }
}
