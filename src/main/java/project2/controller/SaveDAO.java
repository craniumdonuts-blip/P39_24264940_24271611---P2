package project2.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import project2.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ella
 */
public class SaveDAO {

    // Gets the single database connection from DatabaseManager
    private Connection connection;

    public SaveDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    // Saves player data to the chosen slot
    public void save(Player player, String sceneId, int slot) {
        delete(slot);
        String sql = "INSERT INTO saves (slot, player_name, trait, points, scene_id, inventory) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            stmt.setString(2, player.getName());
            stmt.setString(3, player.getTrait().name());
            stmt.setInt(4, player.getTotalPoints());
            stmt.setString(5, sceneId);
            stmt.setString(6, player.getInventory().toSaveString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    // Loads player data from chosen slot
    public Player load(int slot) {
        String sql = "SELECT * FROM saves WHERE slot = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Player player = new Player();
                player.setName(rs.getString("player_name"));
                player.setTrait(TraitType.valueOf(rs.getString("trait")));
                player.changeTotalPoints(rs.getInt("points"));
                player.getInventory().fromSaveString(rs.getString("inventory"));
                // Store sceneId so GameController can resume from correct scene
                lastLoadedSceneId = rs.getString("scene_id");
                return player;
            }
        } catch (SQLException e) {
            System.out.println("Load failed: " + e.getMessage());
        }
        return null;
    }

    // Deletes save in chosen slot
    public void delete(int slot) {
        String sql = "DELETE FROM saves WHERE slot = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    // Returns true if a slot has saved data
    public boolean slotExists(int slot) {
        String sql = "SELECT slot FROM saves WHERE slot = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, slot);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    // Returns list of slot numbers that have save data
    public List<Integer> listSaves() {
        List<Integer> filled = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            if (slotExists(i)) {
                filled.add(i);
            }
        }
        return filled;
    }

    // Stores the last loaded scene so GameController can resume from it
    private String lastLoadedSceneId;

    public String getLastLoadedSceneId() {
        return lastLoadedSceneId;
    }
}
