package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.SubDuty;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class KitchenTask {

    public KitchenDuty getKitchenDuty() {
        return kitchenDuty;
    }

    private KitchenDuty kitchenDuty;
    private KitchenShift shift;
    private double quantity;
    private int portions;

    private int subDutyId;
    private int kitchenShiftId;
    private int userId;
    private String timeEstimate;

    private User cook;

    public int getId() {
        return id;
    }

    private int position;
    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public KitchenTask(KitchenDuty kitchenDuty,int position) {
        this.kitchenDuty = kitchenDuty;
        this.position = position;
        this.id = 0;
    }

    private KitchenTask(){

    }

    @Override
    public String toString() {
        return "KitchenTask{" +
                "kitchenDuty=" + kitchenDuty +
                ", shift=" + shift +
                ", quantity=" + quantity +
                ", portions=" + portions +
                ", timeEstimate='" + timeEstimate + '\'' +
                ", cook=" + cook +
                ", position=" + position +
                ", id=" + id +
                '}';
    }

//    @Override
//    public String toString() {
//        return kitchenDuty.toString();
//    }


    public static void saveAllKitchenTasks(int summarySheetId, ArrayList<KitchenTask> tasks){
        String kitchenTaskInsert = "INSERT INTO catering.kitchentasks (summarysheet_id, subduty_id,position) VALUES (?, ?,?);";
        PersistenceManager.executeBatchUpdate(kitchenTaskInsert, tasks.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, summarySheetId);
                ps.setInt(2, (tasks.get(batchCount).getKitchenDuty().getId()));
                ps.setInt(3, (tasks.get(batchCount).position));
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                tasks.get(count).id = rs.getInt(1);
            }
        });
    }

    public static ArrayList<KitchenTask> getTasksForSummarySheet(int summarysheet_id){
        String query = "SELECT * FROM kitchentasks WHERE summarysheet_id = '"+summarysheet_id+"' ORDER BY position";
        ArrayList<KitchenTask> kitchenTasks = new ArrayList<>();
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {

                KitchenTask kitchenTask = new KitchenTask();
                kitchenTask.id = rs.getInt("id");
                kitchenTask.quantity = rs.getDouble("quantity");
                kitchenTask.position = rs.getInt("position");
                kitchenTask.portions = rs.getInt("portions");
                kitchenTask.subDutyId = rs.getInt("subduty_id");
                kitchenTask.kitchenShiftId = rs.getInt("kitchenshift_id");
                kitchenTask.userId = rs.getInt("user_id");
                kitchenTask.timeEstimate = rs.getString("timeEstimate");
                kitchenTasks.add(kitchenTask);
            }
        });

        for(KitchenTask kitchenTask : kitchenTasks){
            kitchenTask.kitchenDuty = SubDuty.loadSubDutyById(kitchenTask.subDutyId);
            if(kitchenTask.kitchenShiftId > 0){
                kitchenTask.shift = KitchenShift.loadKitchenShiftByID(kitchenTask.kitchenShiftId);
            }
            if(kitchenTask.userId > 0)
                kitchenTask.cook = User.loadUserById(kitchenTask.userId);
        }

        return kitchenTasks;
    }

    public static void saveNewKitchenTask(KitchenTask kitchenTask,int summarySheetId,int subDutyId){
        String kitchenTaskInsert = "INSERT INTO catering.kitchentasks (summarysheet_id, subduty_id,position) VALUES ("+summarySheetId+", "+subDutyId+","+kitchenTask.position+");";
        int res = PersistenceManager.executeUpdate(kitchenTaskInsert);
        if(res > 0 ){
            kitchenTask.id = PersistenceManager.getLastId();
        }
    }


    public static void deleteKitchenTask(KitchenTask kitchenTask){
        if(kitchenTask.hasCook()){
            KitchenShift.addKitchenAvailability(kitchenTask.getShift().getId(),kitchenTask.cook.getId());
        }
        String delKitchenTask = "DELETE FROM kitchentasks WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(delKitchenTask);
    }

    public static void fillKitchenTask(SummarySheet summarySheet, KitchenTask kitchenTask) {
        String fillKitchenTask = "UPDATE kitchentasks " +
                "SET quantity = " + kitchenTask.quantity + ", " +
                "portions = " + kitchenTask.portions + ", " +
                "kitchenshift_id = " + kitchenTask.shift.getId() + ", " +
                "timeEstimate = '" + kitchenTask.timeEstimate + "' WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(fillKitchenTask);
    }

    public static void moveKitchenTask(SummarySheet currentSheet, KitchenTask kitchenTask, KitchenShift shift) {
        String moveKitchenTask = "UPDATE kitchentasks " +
                "SET kitchenshift_id = " + shift.getId() + " WHERE id = " + kitchenTask.id;;
        if(kitchenTask.hasCook()) {
            KitchenShift.addKitchenAvailability(kitchenTask.shift.getId(), kitchenTask.cook.getId());
            if(kitchenTask.getCook().isKitchenAvailable(shift)) {
                KitchenShift.removeKitchenAvailability(shift.getId(), kitchenTask.getCook().getId());
            } else {
                moveKitchenTask = "UPDATE kitchentasks " +
                        "SET kitchenshift_id = " + shift.getId() + ", user_id = 0 WHERE id = " + kitchenTask.id;
            }
        }
        PersistenceManager.executeUpdate(moveKitchenTask);
    }

    public static void removeKitchenTaskShift(SummarySheet currentSheet, KitchenTask kitchenTask) {
        String removeKitchenTaskShift = "UPDATE kitchentasks " +
                "SET kitchenshift_id = 0, user_id = 0 WHERE id = " + kitchenTask.id;
        if(kitchenTask.hasCook()) {
            KitchenShift.addKitchenAvailability(kitchenTask.shift.getId(), kitchenTask.cook.getId());
        }
        PersistenceManager.executeUpdate(removeKitchenTaskShift);
    }

    public static void changeQuantity(SummarySheet currentSheet, KitchenTask kitchenTask) {
        String changeQuantity = "UPDATE kitchentasks " +
                "SET quantity = " + kitchenTask.quantity + " WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(changeQuantity);
    }

    public static void changePortions(SummarySheet currentSheet, KitchenTask kitchenTask) {
        String changePortions = "UPDATE kitchentasks " +
                "SET portions = " + kitchenTask.portions + " WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(changePortions);
    }

    public static void changeTimeEstimate(SummarySheet currentSheet, KitchenTask kitchenTask) {
        String changeTimeEstimate = "UPDATE kitchentasks " +
                "SET timeEstimate = '" + kitchenTask.timeEstimate + "' WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(changeTimeEstimate);
    }

    public static void assignCookToTask(SummarySheet currentSheet, KitchenTask kitchenTask) {
        String assignCookToTask = "UPDATE kitchentasks " +
                "SET user_id = " + kitchenTask.cook.getId() + " WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(assignCookToTask);
        KitchenShift.removeKitchenAvailability(kitchenTask.shift.getId(), kitchenTask.cook.getId());
    }

    public static void removeCookFromTask(SummarySheet currentSheet, KitchenTask kitchenTask) {
        String removeCookFromTask = "UPDATE kitchentasks " +
                "SET user_id = 0 WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(removeCookFromTask);
        KitchenShift.addKitchenAvailability(kitchenTask.shift.getId(), kitchenTask.cook.getId());
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setShift(KitchenShift shift) {
        this.shift = shift;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public void setTimeEstimate(String timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public KitchenShift getShift() {
        return this.shift;
    }

    public User getCook() {
        return this.cook;
    }

    public boolean hasCook() {
        return cook != null;
    }

    public void changeShift(KitchenShift shift) {
        if(this.hasCook()) {
            this.cook.addKitchenAvailability(this.shift);
            if(this.cook.isKitchenAvailable(shift)) {
                this.cook.removeKitchenAvailability(shift);
            } else {
                this.cook = null;
            }
        }

        this.shift = shift;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KitchenTask that = (KitchenTask) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void removeCook() {
        this.cook.addKitchenAvailability(this.shift);
        this.cook = null;
    }

    public void removeShift() {
        this.shift = null;
    }

    public void assignCook(User cook) {
        this.cook = cook;
        cook.removeKitchenAvailability(this.shift);
    }
}
