package config;
import java.sql.*;


public class DatabaseConfig {
    public static final String URL = System.getenv("DB_URL") != null
        ? System.getenv("DB_URL")
        : "jdbc:postgresql://localhost:5432/mydb";
    public static final String USER = System.getenv("DB_USER") != null
        ? System.getenv("DB_USER")
        : "myuser";
    public static final String PASSWORD = System.getenv("DB_PASS") != null
        ? System.getenv("DB_PASS")
        : "mypass";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);

            }
        }catch (SQLException e){
            throw new SQLException("postgresql driver not found", e);
        }
        return connection;
    }
    public static void closeConnection() throws SQLException {
        if (connection != null &&  !connection.isClosed()) {
            connection.close();
        }
    }

}
