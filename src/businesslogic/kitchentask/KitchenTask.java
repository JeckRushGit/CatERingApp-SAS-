package businesslogic.kitchentask;

import businesslogic.recipe.KitchenDuty;
import businesslogic.recipe.SubDuty;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

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

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public KitchenTask(KitchenDuty kitchenDuty) {
        this.kitchenDuty = kitchenDuty;
    }

    public static void saveAllKitchenTasks(int summarySheetId, ArrayList<KitchenTask> tasks){
        String kitchenTaskInsert = "INSERT INTO catering.kitchentasks (summarysheet_id, subduty_id) VALUES (?, ?);";
        PersistenceManager.executeBatchUpdate(kitchenTaskInsert, tasks.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, summarySheetId);
                ps.setInt(2, (tasks.get(batchCount).getKitchenDuty().getId()));
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                tasks.get(count).id = rs.getInt(1);
            }
        });
    }
}
