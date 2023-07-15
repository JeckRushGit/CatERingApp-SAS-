package businesslogic.user;

import businesslogic.shift.KitchenShift;
import businesslogic.shift.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {

    private static Map<Integer, User> loadedUsers = FXCollections.observableHashMap();

    public static enum Role {SERVIZIO, CUOCO, CHEF, ORGANIZZATORE};

    private int id;
    private String username;
    private Set<Role> roles;
    private List<KitchenShift> kitchenAvailabilities;

    public User() {
        id = 0;
        username = "";
        this.roles = new HashSet<>();
    }

    public boolean isChef() {
        return roles.contains(Role.CHEF);
    }

    public String getUserName() {
        return username;
    }

    public int getId() {
        return this.id;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public String toString() {
        String result = username;
        if (roles.size() > 0) {
            result += ": ";

            for (User.Role r : roles) {
                result += r.toString() + " ";
            }
        }
        return result;
    }

    public void addKitchenAvailability(KitchenShift shift) {
        this.kitchenAvailabilities.add(shift);
    }

    public void removeKitchenAvailability(KitchenShift shift) {
        this.kitchenAvailabilities.remove(shift);
    }

    public boolean isKitchenAvailable(KitchenShift shift) {
        return this.kitchenAvailabilities.contains(shift);
    }

    public List<KitchenShift> getKitchenAvailabilities() {
        return kitchenAvailabilities;
    }
    // STATIC METHODS FOR PERSISTENCE

    public static User loadUserById(int uid) {
        if (loadedUsers.containsKey(uid)) return loadedUsers.get(uid);

        User load = new User();
        String userQuery = "SELECT * FROM Users WHERE id='"+uid+"'";
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                load.id = rs.getInt("id");
                load.username = rs.getString("username");
            }
        });
        if (load.id > 0) {
            loadedUsers.put(load.id, load);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + load.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            load.roles.add(User.Role.CUOCO);
                            load.kitchenAvailabilities = User.loadUserKitchenAvailabilityByID(load.id);
                            break;
                        case 'h':
                            load.roles.add(User.Role.CHEF);
                            break;
                        case 'o':
                            load.roles.add(User.Role.ORGANIZZATORE);
                            break;
                        case 's':
                            load.roles.add(User.Role.SERVIZIO);
                    }
                }
            });
        }
        return load;
    }

    public static User loadUser(String username) {
        User u = new User();
        String userQuery = "SELECT * FROM Users WHERE username='"+username+"'";
        PersistenceManager.executeQuery(userQuery, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                u.id = rs.getInt("id");
                u.username = rs.getString("username");
            }
        });
        if (u.id > 0) {
            loadedUsers.put(u.id, u);
            String roleQuery = "SELECT * FROM UserRoles WHERE user_id=" + u.id;
            PersistenceManager.executeQuery(roleQuery, new ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    String role = rs.getString("role_id");
                    switch (role.charAt(0)) {
                        case 'c':
                            u.roles.add(User.Role.CUOCO);
                            u.kitchenAvailabilities = User.loadUserKitchenAvailabilityByID(u.id);
                            break;
                        case 'h':
                            u.roles.add(User.Role.CHEF);
                            break;
                        case 'o':
                            u.roles.add(User.Role.ORGANIZZATORE);
                            break;
                        case 's':
                            u.roles.add(User.Role.SERVIZIO);
                    }
                }
            });
        }
        return u;
    }

    public static ObservableList<KitchenShift> loadUserKitchenAvailabilityByID(int user_id) {
        ObservableList<KitchenShift> res = FXCollections.observableArrayList();
        String query = "SELECT kitchenshifts.start, kitchenshifts.end, kitchenshifts.id " +
                "FROM (kitchenshifts JOIN kitchenavailabilities ON kitchenshifts.id = kitchenavailabilities.kitchenshift_id) " +
                "WHERE user_id = " + user_id + ";";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                KitchenShift sh = new KitchenShift(rs.getString("start"), rs.getString("end"));
                sh.setId(rs.getInt("id"));
                res.add(sh);
            }
        });
        return res;
    }
}
