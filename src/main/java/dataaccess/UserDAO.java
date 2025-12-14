package dataaccess;
import config.DatabaseConfig;
import datatransfer.MediaRequest;
import models.MediaEntry;
import models.User;

import java.sql.*;
import java.util.*;
import java.util.List;

public class UserDAO {
    MediaDAO mediaDAO = new MediaDAO();

    public User create(Connection con, User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            int affected = stmt.executeUpdate();
            if (affected == 0) throw new SQLException("Insert failed");
            return user;

        } catch (SQLException e) {
            throw new SQLDataException(e.getMessage());
        }

    }
    public User findByUsername(Connection con,String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password_hash"));
                user.setToken(rs.getString("token"));
                user.setFavoriteGenre(rs.getString("favoritegenre"));
                return user;
            }
            return null;

        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
    }

    public User findByUserID(Connection con, int userID) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {  // ← Execute AFTER setting parameter
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setToken(rs.getString("token"));
                    user.setFavoriteGenre(rs.getString("favoritegenre"));
                    return user;
                }
                return null;
            }

        }catch (SQLException e){
            throw new SQLDataException(e.getMessage());
        }
    }

    public List<User> getUsers(Connection con) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT username, password_hash FROM users";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
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
    public User editUser(Connection con, User user, String columnName, String newValue) throws SQLException {
        String sql = "UPDATE users SET " + columnName + " = ? WHERE username = ?";
        try(PreparedStatement stmt = con.prepareStatement(sql);){
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
    public List<MediaRequest> findFavoritesByUserID(Connection con, int userID) throws SQLException {
        List<MediaRequest> favorites = new ArrayList<>();
        String sql = "SELECT m.* FROM media_entries m " +
                "JOIN favorites f ON m.id = f.media_id " +
                "WHERE f.user_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                favorites.add(mediaDAO.mapResultSetToMediaRequest(rs));

            }
        }catch(SQLException e){
            throw new SQLDataException(e.getMessage());
        }
        return favorites;
    }

    public void updateToken(Connection con, String username, String token) {
        String sql =  "UPDATE users SET token = ? WHERE username = ?";
        try(PreparedStatement stmt = con.prepareStatement(sql)) {
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

