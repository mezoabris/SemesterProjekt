package service;

import dataaccess.UserDAO;
import datatransfer.AuthRequest;

import helpers.ConnectionProvider;
import helpers.PasswordHasher;
import helpers.TokenHelper;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class AuthService {
    private final PasswordHasher passwordHasher;
    private final UserDAO userDAO;
    private final ConnectionProvider connectionProvider;

    public AuthService(PasswordHasher passwordHasher, UserDAO userDAO, ConnectionProvider connectionProvider) { // <--- NEW
        this.passwordHasher = passwordHasher;
        this.userDAO = userDAO;
        this.connectionProvider = connectionProvider;
    }
    public String register(AuthRequest req) throws SQLException {
        String token = null;
        Connection conn = null;
        try{
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);
            User existingUser = userDAO.findByUsername(conn, req.getUsername());
            if(existingUser != null) {
                throw new SQLException("Username already exists");

            }
            User newUser = new User();
            newUser.setUsername(req.getUsername());
            newUser.setPassword(passwordHasher.hashPassword(req.getPassword()));

            User createdUser =  userDAO.create(conn, newUser);
            if(createdUser != null) {
                token = TokenHelper.generateToken(createdUser.getUsername());
                userDAO.updateToken(conn, createdUser.getUsername(), token);

            }

            conn.commit();
        }catch ( SQLException e){
            if(conn != null){
                try{
                    conn.rollback();
                    System.out.println("transaction rolled back due to error:" + e.getMessage());
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }

        }finally {
            if(conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return token;
    }
    public User login(AuthRequest req) throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){

            User existingUser = userDAO.findByUsername(conn, req.getUsername());
            if(existingUser == null) {
                throw new SQLException("User not found");
            }
            String token = TokenHelper.generateToken(existingUser.getUsername());
            userDAO.updateToken(conn, existingUser.getUsername(), token);
            existingUser.setToken(token);
            return existingUser;
        }
    }
    public boolean isTokenValid(int userID, String token) throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){

            User user = userDAO.findByUserID(conn, userID);
            return Objects.equals(token, user.getToken());
        }

    }
    public User findUserByToken(String token) throws SQLException {
        User user;
        try(Connection conn = connectionProvider.getConnection()) {
            String username = TokenHelper.getUserName(token);
            if (username == null) {
                throw new SQLException("Invalid token format");
            }
            user = userDAO.findByUsername(conn, username);
            if (user == null || !Objects.equals(user.getToken(), token)) {
                throw new SQLException("Invalid token");
            }
        } catch (SQLException e) {
            throw new SQLException("Invalid token", e);
        }
        return user;
    }
}
