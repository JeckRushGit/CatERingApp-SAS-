package businesslogic.shift;

import businesslogic.kitchentask.KitchenTask;
import businesslogic.recipe.SubDuty;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class KitchenShift extends Shift{
    private boolean full;
    private int id;

    public KitchenShift(String start, String end) {
        super(start, end);
        this.full = false;
    }



    public boolean isFull() {
        return this.full;
    }

    public int getId() {
        return this.id;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KitchenShift that = (KitchenShift) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return super.toString() + "  id = " + this.id;
    }

    public static KitchenShift loadKitchenShiftByID(int kitchenshift_id){
        String query = "SELECT *" +
                "FROM kitchenshifts WHERE ID = " + kitchenshift_id;
        KitchenShift kitchenShift = new KitchenShift("", "");
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                ArrayList<KitchenTask> tasks = new ArrayList<>();
                kitchenShift.id = rs.getInt("id");
                kitchenShift.setStart(rs.getString("start"));
                kitchenShift.setEnd(rs.getString("end"));
                kitchenShift.setFull(rs.getBoolean("full"));
            }
        });

        return kitchenShift;
    }

    public static void removeKitchenAvailability(int shift_id, int user_id) {
        String query = "DELETE FROM kitchenavailabilities WHERE kitchenshift_id = " + shift_id + " AND user_id = " + user_id;
        PersistenceManager.executeUpdate(query);
    }

    public static void addKitchenAvailability(int shift_id, int user_id) {
        String query = "INSERT INTO kitchenavailabilities (kitchenshift_id, user_id) VALUES (" + shift_id + ", " + user_id +")";
        PersistenceManager.executeUpdate(query);
    }
}
