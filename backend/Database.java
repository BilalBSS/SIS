package backend;

import java.sql.*;

public class Database {
    private Connection conn;

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/UserAccounts", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void createTable() {
        try {
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS UserAccounts " +
                         "(Username VARCHAR(255) PRIMARY KEY NOT NULL," +
                         " Password VARCHAR(255) NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insertUser(String username, String password) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO UserAccounts(Username,Password) VALUES(?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM UserAccounts WHERE Username = ? AND Password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
