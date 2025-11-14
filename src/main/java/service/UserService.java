package service;
import dataaccess.UserDAO;
import datatransfer.FavoritesRequest;
import datatransfer.MediaRequest;
import datatransfer.UserProfile;
import datatransfer.UserResponse;
import helpers.GenreValidation;
import models.MediaEntry;
import models.User;
import helpers.PasswordHasher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {
    PasswordHasher passwordHasher = new PasswordHasher();
    GenreValidation validator = new GenreValidation();
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
    public FavoritesRequest findUserFavorites(int userID) throws SQLException {
        List<MediaRequest> favorites = userDAO.findFavoritesByUserID(userID);
        FavoritesRequest favoriteReq = new FavoritesRequest();
        favoriteReq.setFavorites(favorites);


        return favoriteReq;

    }
    public UserResponse editProfile(int userID, UserProfile changes) throws SQLException {
        UserResponse userResponse = new UserResponse();
        User user = new  User();
        User curr = userDAO.findByUserID(userID);
        boolean nameDifference = !Objects.equals(curr.getUsername(), changes.getUsername());
        boolean genreDifference = !Objects.equals(curr.getFavoriteGenre(), changes.getFavoritegenre());
        if(!nameDifference && !genreDifference){
            userResponse.setStatus(200);
            userResponse.setMessage("You haven't made any changes");
        }
        if(genreDifference){
            if(!validator.validateGenre(changes.getFavoritegenre())){
                userResponse.setStatus(400);
                userResponse.setMessage("Please enter a valid genre");
                return userResponse;
            }
            curr = userDAO.editUser(curr, "favoritegenre", changes.getFavoritegenre());
        }
        if(nameDifference){
            User existing = userDAO.findByUsername(changes.getUsername());
            if(existing != null && existing.getUserID() != userID){
                userResponse.setStatus(400);
                userResponse.setMessage("Username already taken");
                return userResponse;
            }
            curr =  userDAO.editUser(curr, "username", changes.getUsername());
        }
        userResponse.setStatus(200);
        userResponse.setMessage("Profile updated successfully");
        return userResponse;

    }

}
