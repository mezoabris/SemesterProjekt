package dataaccess;
import config.DatabaseConfig;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {


    public User create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            int affected = stmt.executeUpdate();
            if (affected == 0) throw new SQLException("Insert failed");
            return user;

        } catch (SQLException e) {
            throw new SQLDataException(e.getMessage());
        }

    }
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password_hash"));
                user.setToken(rs.getString("token"));
                return user;
            }
            return null;

        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
    }
    public User findByToken(String token) throws SQLException {
        String sql = "SELECT * FROM users WHERE token = ?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password_hash"));
                user.setToken(rs.getString("token"));
                return user;
            }
            return null;

        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
    }

    public User findByUserID(int userID) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = DatabaseConfig.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {  // ← Execute AFTER setting parameter
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setToken(rs.getString("token"));
                    return user;
                }
                return null;
            }

        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
    }

    public List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, password_hash FROM users";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
             ResultSet rs = stmt.executeQuery();
             while(rs.next()) {

                 String username = rs.getString("username");
                 String passwordHash = rs.getString("password_hash");
                 User user = new User(username, passwordHash);
                 users.add(user);
             }
        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
        return users;
    }
    public User editUser(User user, String columnName, String newValue) throws SQLException {
        String sql = "UPDATE users SET " + columnName + " = ? WHERE username = ?";
        try(Connection con = DatabaseConfig.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);){
            stmt.setString(1, newValue);
            stmt.setString(2, user.getUsername());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("No user found with username: " + user.getUsername());
            }
        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
        return user;
    }


    public void updateToken(String username, String token) {
        String sql =  "UPDATE users SET token = ? WHERE username = ?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setString(2, username);
            int affected = stmt.executeUpdate(); // <-- use executeUpdate
            if (affected == 0) {
                throw new SQLException("No user found to update token for username: " + username);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

