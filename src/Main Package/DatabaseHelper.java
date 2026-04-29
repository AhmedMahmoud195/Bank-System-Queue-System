//package MainPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "DB209";
    private static final String PASSWORD = "DB209";

    public static void initializeDatabase() {
        String createSequenceSQL = "CREATE SEQUENCE tickets_seq " +
                "START WITH 1 " +
                "INCREMENT BY 1 " +
                "NOCACHE " +
                "NOCYCLE";


        String createTableSQL = "CREATE TABLE tickets (" +
                "id NUMBER PRIMARY KEY, " +
                "customer_name VARCHAR2(100) NOT NULL, " +
                "priority NUMBER(10) NOT NULL)";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            try {
                stmt.execute(createSequenceSQL);
                System.out.println("Sequence created successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) { // already exists
                    System.out.println("Sequence already exists.");
                }
            }


            try {
                stmt.execute(createTableSQL);
                System.out.println("Table created successfully.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 955) { // already exists
                    System.out.println("Table already exists.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void saveTicketToDB(String customerName, int priority){
        String insertSQL = "INSERT INTO tickets(id, customer_name, priority) " +
                "VALUES(tickets_seq.NEXTVAL, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, customerName);
            pstmt.setInt(2, priority);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Failed to save ticket: " + e.getMessage());
        }
    }
}