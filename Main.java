import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import backend.*;
import frontend.*;

public class Main {
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String USER = "root";
    static final String PASS = "1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();) {

            // Check if the database exists
            ResultSet rs = stmt.executeQuery("SHOW DATABASES LIKE 'UserAccounts'");

            // If the database does not exist, create it
            if (!rs.next()) {
                stmt.executeUpdate("CREATE DATABASE UserAccounts");
                System.out.println("Database created successfully...");
            } else {
                System.out.println("Database already exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new LoginFrame();
    }
}
