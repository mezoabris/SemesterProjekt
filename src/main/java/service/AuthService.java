package service;

import dataaccess.UserDAO;
import datatransfer.AuthRequest;

import helpers.PasswordHasher;
import helpers.TokenHelper;
import models.User;

import java.sql.SQLException;
import java.util.Objects;

public class AuthService {
    PasswordHasher passwordHasher = new PasswordHasher();
    private UserDAO userDAO = new UserDAO();

    public String register(AuthRequest req) throws SQLException {
        User existingUser = userDAO.findByUsername(req.getUsername());
        if(existingUser != null) {
            throw new SQLException("Username already exists");

        }
        User newUser = new User();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(passwordHasher.hashPassword(req.getPassword()));

        User createdUser =  userDAO.create(newUser);
        if(createdUser != null) {
            String token = TokenHelper.generateToken(createdUser.getUsername());
            userDAO.updateToken(createdUser.getUsername(), token);
            return token;
        }

        throw new RuntimeException("Failed to create user");

    }
    public User login(AuthRequest req) throws SQLException {
        User existingUser = userDAO.findByUsername(req.getUsername());
        if(existingUser == null) {
            throw new SQLException("User not found");
        }
        String token = TokenHelper.generateToken(existingUser.getUsername());
        userDAO.updateToken(existingUser.getUsername(), token);
        existingUser.setToken(token);
        return existingUser;
    }
    public boolean isTokenValid(int userID, String token) throws SQLException {
        User user = userDAO.findByUserID(userID);
        return Objects.equals(token, user.getToken());

    }
    public User findUserByToken(String token) throws SQLException {
        User user;
        try {
            user = userDAO.findByToken(token);
        } catch (SQLException e) {
            throw new SQLException("Invalid token");
        }
        return user;
    }
}
