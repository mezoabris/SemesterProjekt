package service;
import dataaccess.RatingDAO;
import dataaccess.UserDAO;
import datatransfer.*;
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
    private final RatingDAO ratingDAO;
    ConnectionProvider connectionProvider;
    public UserService(GenreValidation validator, UserDAO userDAO, RatingDAO ratingDAO, ConnectionProvider connectionProvider ){
        this.validator = validator;
        this.userDAO = userDAO;
        this.ratingDAO = ratingDAO;
        this.connectionProvider = connectionProvider;
    }
    public List<User> findAll() throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){

            List<User> userList = new ArrayList<>();
            userList = userDAO.getUsers(conn);
            return userList;
        }
    }
    public UserResponse findUserProfile(int userID) throws SQLException {
        try(Connection conn = connectionProvider.getConnection()){
            User user = userDAO.findByUserID(conn, userID);
            List<RatingRequest> ratings = ratingDAO.getUserRatings(conn, userID);
            double ratingsSum = 0;
            for(RatingRequest rating: ratings){
                ratingsSum+= rating.getStars();
            }
            UserResponse response = new UserResponse();
            response.setRatings(ratings);
            response.setTotalRatings(ratings.size());
            if(!ratings.isEmpty()){
                response.setAverageScore(ratingsSum / ratings.size());
            }else{
                response.setAverageScore(0.0);
            }
            response.setFavorites(userDAO.findFavoritesByUserID(conn, userID));
            if (user != null) {
                response.setStatus(200);
                response.setUser(user);
                response.setMessage("User profile found");
            } else {
                response.setStatus(404);
                response.setMessage("User not found");
            }
            return response;
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
