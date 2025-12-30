package service;

import dataaccess.MediaDAO;
import dataaccess.RatingDAO;
import dataaccess.UserDAO;
import datatransfer.MediaRequest;
import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import helpers.ConnectionProvider;
import models.Rating;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingService {
    private final RatingDAO ratingDAO;
    private final MediaDAO mediaDAO; // Use MediaDAO directly
    private final ConnectionProvider connectionProvider;

    public RatingService(RatingDAO ratingDAO, MediaDAO mediaDAO, ConnectionProvider connectionProvider){ // Update constructor
        this.ratingDAO = ratingDAO;
        this.mediaDAO = mediaDAO;
        this.connectionProvider = connectionProvider;
    }

    public List<RatingRequest> findAllRatings(int mediaID) throws SQLException {
        List<RatingRequest> res = new ArrayList<>();
        try(Connection con = connectionProvider.getConnection()){
            res =  ratingDAO.getMediaRatings(con, mediaID);
        }
        return res;
    }

    public RatingResponse saveRating(int userID, Rating ratingRequest) throws SQLException {
        System.out.println("rating service reached");
        try(Connection con = connectionProvider.getConnection()){
            con.setAutoCommit(false); // Start transaction

            Rating existing = ratingDAO.findByUserAndMedia(con, userID, ratingRequest.getMediaID());
            if(existing == null){

                RatingResponse response = ratingDAO.createRating(con, userID, ratingRequest);
                if (response.getStatus() == 200) {
                    con.commit();
                } else {
                    con.rollback();
                }
                return response;
            }else{
                System.out.println("there is already a rating with "+ratingRequest.getMediaID()+" mediaid and "+ userID+" userID");

                RatingResponse response = ratingDAO.updateRating(con, existing.getMediaID(), ratingRequest, userID);
                if (response.getStatus() == 200) {
                    con.commit();
                }
                else {
                    con.rollback();
                }
                return response;
            }
        }
    }

    public RatingResponse approveRating(int ratingID, String approverUsername) throws SQLException {
        RatingResponse response = new RatingResponse();
        try (Connection con = connectionProvider.getConnection()) {
            con.setAutoCommit(false); // Start transaction

            Rating rating = ratingDAO.findById(con, ratingID);
            if (rating == null) {
                response.setStatus(404);
                response.setMessage("Rating not found.");
                con.rollback();
                return response;
            }

            // Use MediaDAO directly with the SAME connection
            MediaRequest media = mediaDAO.findById(con, rating.getMediaID());
            
            if (media == null) {
                response.setStatus(500); 
                response.setMessage("Could not retrieve media associated with rating.");
                con.rollback();
                return response;
            }

            if (!media.getCreatorUsername().equals(approverUsername)) {
                response.setStatus(403);
                response.setMessage("Forbidden: Only the media creator can approve comments.");
                con.rollback();
                return response;
            }

            response = ratingDAO.approveComment(con, ratingID);
            if (response.getStatus() == 200) {
                con.commit();
            } else {
                con.rollback();
            }
            return response;

        } catch (SQLException e) {
            response.setStatus(500);
            response.setMessage("Internal server error: " + e.getMessage());
            throw e;
        }
    }

    public RatingResponse removeRating(int ratingID, int userID) throws SQLException {
        RatingResponse response = new RatingResponse();
        try (Connection con = connectionProvider.getConnection()) {
            con.setAutoCommit(false);

            Rating rating = ratingDAO.findById(con, ratingID);
            if (rating == null) {
                response.setStatus(404);
                response.setMessage("Rating not found.");
                con.rollback();
                return response;
            }

            if(rating.getUserID() != userID){
                response.setStatus(403);
                response.setMessage("Forbidden. You can only delete your own rating!");
                con.rollback();
                return response;
            }

            response = ratingDAO.deleteRatingById(con, ratingID);
            if (response.getStatus() == 200) {
                con.commit();
            } else {
                con.rollback();
            }
            return response;

        } catch (SQLException e) {
            response.setStatus(500);
            response.setMessage("Internal server error: " + e.getMessage());
            throw e;
        }
    }
}
