package service;

import dataaccess.UserDAO;
import datatransfer.LoginRequest;
import datatransfer.RegisterRequest;
import hashing.PasswordHasher;
import models.User;

import java.sql.SQLException;

public class AuthService {
    PasswordHasher passwordHasher = new PasswordHasher();
    private UserDAO userDAO = new UserDAO();

    public User register(RegisterRequest req) throws SQLException {
        User user = new User(req.getVorname(), req.getNachname(), req.getUsername(), req.getEmail());
        return user;

    }
    public User login(LoginRequest req) throws SQLException {
        return new User();
    }
}
