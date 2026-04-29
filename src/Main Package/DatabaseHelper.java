package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:bank_queue.db";

    public static void initializeDatabase() {
        // NEW: Added 'service_type' to the table schema
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tickets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_name TEXT NOT NULL, " +
                "priority INTEGER NOT NULL, " +
                "service_type TEXT NOT NULL);"; 

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    // NEW: Added 'String serviceType' to the parameters
    public static void saveTicketToDB(String customerName, int priority, String serviceType) {
        // NEW: Updated SQL to expect 3 values instead of 2
        String insertSQL = "INSERT INTO tickets(customer_name, priority, service_type) VALUES(?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            
            pstmt.setString(1, customerName);
            pstmt.setInt(2, priority);
            pstmt.setString(3, serviceType); // NEW: Bind the service type to the database
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("Failed to save ticket: " + e.getMessage());
        }
    }
}
