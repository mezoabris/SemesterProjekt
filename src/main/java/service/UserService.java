package service;
import dataaccess.UserDAO;
import datatransfer.UserProfileResponse;
import models.User;
import helpers.PasswordHasher;

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
    public User findUserProfile(int userID) throws SQLException {
        User user = new  User();
        user = userDAO.findByUserID(userID);
        return user;

    }

}
