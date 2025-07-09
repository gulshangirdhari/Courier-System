import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Connect to your MySQL database
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/courier_db", "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
