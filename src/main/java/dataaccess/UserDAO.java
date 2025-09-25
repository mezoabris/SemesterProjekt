package dataaccess;
import config.DatabaseConfig;
import models.User;
import hashing.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {


    public User create(User user) throws SQLException {
        String sql = "INSERT INTO users (vorname, nachname, username, password) VALUES (?, ?, ?, ?, ?) RETURNING username, created_at";



        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, user.getVorname());
            stmt.setString(2, user.getNachname());
            stmt.setString(3, user.getUserName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setCreatedAt(rs.getTimestamp("created_at"));
                return user;
            }

        } catch (SQLException e) {
            throw new SQLDataException(e.getMessage());
        }
        return user;
    }

    public List<User> getUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT vorname, nachname, username, email, password_hash FROM users";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
             ResultSet rs = stmt.executeQuery();
             while(rs.next()) {
                 String vorname = rs.getString("vorname");
                 String nachname = rs.getString("nachname");
                 String username = rs.getString("username");
                 String email = rs.getString("email");
                 String passwordHash = rs.getString("password_hash");
                 User user = new User(vorname, nachname, username,email);
                 user.setPassword(passwordHash);
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
            stmt.setString(2, user.getUserName());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("No user found with username: " + user.getUserName());
            }
        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
        return user;
    }

}

