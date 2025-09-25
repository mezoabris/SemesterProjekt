package Database;
import java.sql.*;

public class PostGreSQL {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/mydb";
        String user = "myuser";
        String password = "mypass";
        try(Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT username FROM users  WHERE id = 1;");
            while (rs.next()) {
                String username = rs.getString("username");
                System.out.println("Username: " + username);

            }

        }catch(Exception e){

        }
    }
}
