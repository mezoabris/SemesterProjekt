package service;
import dataaccess.UserDAO;
import datatransfer.FavoritesRequest;
import datatransfer.MediaRequest;
import datatransfer.UserProfile;
import datatransfer.UserResponse;
import helpers.ConnectionProvider;
import helpers.GenreValidation;
import models.MediaEntry;
import models.User;
import helpers.PasswordHasher;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {
    private final GenreValidation validator;
    private final UserDAO userDAO;
    ConnectionProvider connectionProvider;
    public UserService(GenreValidation validator, UserDAO userDAO, ConnectionProvider connectionProvider ){
        this.validator = validator;
        this.userDAO = userDAO;
        this.connectionProvider = connectionProvider;
    }
    public List<User> findAll() throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){

            List<User> userList = new ArrayList<>();
            userList = userDAO.getUsers(conn);
            return userList;
        }
    }
    public User findUserProfile(int userID) throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){

            User user = new  User();
            user = userDAO.findByUserID(conn, userID);
            return user;
        }

    }
    public FavoritesRequest findUserFavorites(int userID) throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){

            List<MediaRequest> favorites = userDAO.findFavoritesByUserID(conn, userID);
            FavoritesRequest favoriteReq = new FavoritesRequest();
            favoriteReq.setFavorites(favorites);


            return favoriteReq;
        }

    }
    public UserResponse editProfile(int userID, UserProfile changes) throws SQLException {
        UserResponse userResponse = new UserResponse();
        Connection conn = null;
        try{
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);

            User curr = userDAO.findByUserID(conn, userID);
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
                    conn.rollback();
                    return userResponse;
                }
                curr = userDAO.editUser(conn, curr, "favoritegenre", changes.getFavoritegenre());
            }
            if(nameDifference){
                User existing = userDAO.findByUsername(conn, changes.getUsername());
                if(existing != null && existing.getUserID() != userID){
                    userResponse.setStatus(400);
                    userResponse.setMessage("Username already taken");
                    conn.rollback();
                    return userResponse;
                }
                curr =  userDAO.editUser(conn, curr, "username", changes.getUsername());
            }
            userResponse.setStatus(200);
            userResponse.setMessage("Profile updated successfully");

            conn.commit();

        }catch (SQLException e){
            if(conn != null){
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to error: "+e.getMessage());

                }catch (SQLException E){
                    E.printStackTrace();
                }
            }
        }finally {
            if(conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
        }

        return userResponse;

    }

}
