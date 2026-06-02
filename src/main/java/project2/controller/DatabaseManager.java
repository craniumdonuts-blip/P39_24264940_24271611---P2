package project2.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ella
 */
public class DatabaseManager {

    // One class instance
    private static DatabaseManager instance;
    // Connect to derby database called gameDB
    private Connection connection;
    private static final String DB_URL = "jdbc:derby:gameDB;create=true";

    // Singleton pattern, private class so that only this class can create itself
    private DatabaseManager() {
        try {
            // Opens connection
            connection = DriverManager.getConnection(DB_URL);
            // Creates tables if they do not exist
            createTables();

        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Only one connection to the database at a time
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Allows other classes to run queries
    public Connection getConnection() {
        return connection;
    }

    // Creates saves table only if it doesn't already exist
    private void createTables() throws SQLException {
        // Check what already exists in the database
        try (ResultSet rs = connection.getMetaData().getTables(null, null, "SAVES", null)) {
            // rs.next() is true if table exists
            if (!rs.next()) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(
                            "CREATE TABLE saves ("
                            + "slot INT PRIMARY KEY, "
                            + // PK: save slot number (1-5)
                            "player_name VARCHAR(100), "
                            + // Player's name
                            "trait VARCHAR(20), "
                            + // Trait Type
                            "points INT, "
                            + // Points
                            "scene_id VARCHAR(50), "
                            + // Current scene
                            "inventory VARCHAR(500))" // Inventory
                    );

                }

            }
        }
    }
}
