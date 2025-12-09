package service;

import dataaccess.RatingDAO;
import dataaccess.UserDAO;
import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import models.Rating;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingService {
    RatingDAO ratingDAO = new RatingDAO();
    public List<RatingRequest> findAllRatings(int userID) throws SQLException {
        return ratingDAO.getUserRatings(userID);
    }

    public RatingResponse saveRating(int userID, Rating ratingRequest) {
        System.out.println("rating service reached");
        Rating existing = ratingDAO.findByUserAndMedia(userID, ratingRequest.getMediaID());
        if(existing == null){
            System.out.println("rating not found");

            return ratingDAO.createRating(userID, ratingRequest);
        }else{
            System.out.println("there is already a rating with "+ratingRequest.getMediaID()+" mediaid and "+ userID+" userID");

            return ratingDAO.updateRating(existing.getMediaID(), ratingRequest, userID);
        }
    }
}
