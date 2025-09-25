package service;
import dataaccess.UserDAO;
import models.User;
import hashing.PasswordHasher;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    PasswordHasher passwordHasher = new PasswordHasher();
    private UserDAO userDAO = new UserDAO();
    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        userList = userDAO.getUsers();
        return userList;
    }

    public User editUserData(User user, String columnName, String newValue) throws SQLException {
        List<String> allowedColumns = List.of("vorname", "nachname", "email", "password_hash");
        if(!allowedColumns.contains(columnName)) {
            throw new SQLDataException("Invalid column name");
        }
        if(columnName.equals("password_hash")) {
            newValue = passwordHasher.hashPassword(newValue);
        }
        user = userDAO.editUser(user, columnName, newValue);
        switch (columnName) {
            case "vorname": user.setVorname(newValue); break;
            case "nachname": user.setNachname(newValue); break;
            case "email": user.setEmail(newValue); break;
            case "password": user.setPassword(newValue); break;
        }
        return user;
    }
}
