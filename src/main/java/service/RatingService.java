package service;

import dataaccess.RatingDAO;
import dataaccess.UserDAO;
import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import helpers.ConnectionProvider;
import models.Rating;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingService {
    private final RatingDAO ratingDAO;
    private final ConnectionProvider connectionProvider;
    public RatingService(RatingDAO ratingDAO, ConnectionProvider connectionProvider){
        this.ratingDAO = ratingDAO;
        this.connectionProvider = connectionProvider;
    }
    public List<RatingRequest> findAllRatings(int userID) throws SQLException {
        List<RatingRequest> res = new ArrayList<>();
        try(Connection con = connectionProvider.getConnection()){
            res =  ratingDAO.getUserRatings(con, userID);
        }
        return res;
    }

    public RatingResponse saveRating(int userID, Rating ratingRequest) throws SQLException {
        System.out.println("rating service reached");
        try(Connection con = connectionProvider.getConnection()){

            Rating existing = ratingDAO.findByUserAndMedia(con, userID, ratingRequest.getMediaID());
            if(existing == null){
                System.out.println("rating not found");

                return ratingDAO.createRating(con, userID, ratingRequest);
            }else{
                System.out.println("there is already a rating with "+ratingRequest.getMediaID()+" mediaid and "+ userID+" userID");

                return ratingDAO.updateRating(con, existing.getMediaID(), ratingRequest, userID);
            }
        }
    }
}
