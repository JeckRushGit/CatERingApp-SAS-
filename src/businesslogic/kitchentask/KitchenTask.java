package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.SubDuty;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class KitchenTask {
    public KitchenDuty getKitchenDuty() {
        return kitchenDuty;
    }

    private KitchenDuty kitchenDuty;

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

    @Override
    public String toString() {
        return kitchenDuty.toString();
    }

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
                SubDuty subDuty = SubDuty.loadSubDutyById(rs.getInt("subduty_id"));
                KitchenTask kitchenTask = new KitchenTask(subDuty,rs.getInt("position"));
                kitchenTask.id = rs.getInt("id");
                kitchenTasks.add(kitchenTask);
            }
        });
        return kitchenTasks;
    }

    public static void saveNewKitchenTask(KitchenTask kitchenTask,int summarySheetId,int subDutyId){
        String kitchenTaskInsert = "INSERT INTO catering.kitchentasks (summarysheet_id, subduty_id,position) VALUES ("+summarySheetId+", "+subDutyId+","+kitchenTask.position+");";
        int res = PersistenceManager.executeUpdate(kitchenTaskInsert);
        if(res > 0 ){
            kitchenTask.id = PersistenceManager.getLastId();
        }
    }


    public static void deleteKitchenTask(SummarySheet summarySheet,KitchenTask kitchenTask){
        String delKitchenTask = "DELETE FROM kitchentasks WHERE id = " + kitchenTask.id;
        PersistenceManager.executeUpdate(delKitchenTask);
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
