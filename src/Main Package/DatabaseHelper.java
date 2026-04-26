package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:bank_queue.db";

    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_name TEXT NOT NULL, " +
                "priority INTEGER NOT NULL);";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void saveTicketToDB(String customerName, int priority) {
        String insertSQL = "INSERT INTO tickets(customer_name, priority) VALUES(?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            pstmt.setString(1, customerName);
            pstmt.setInt(2, priority);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Failed to save ticket: " + e.getMessage());
        }
    }
}
