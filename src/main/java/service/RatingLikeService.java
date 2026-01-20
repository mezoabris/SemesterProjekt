package service;

import dataaccess.MediaDAO;
import dataaccess.RatingDAO;
import dataaccess.RatingLikeDAO;
import dataaccess.UserDAO;
import datatransfer.MediaRequest;
import datatransfer.RatingResponse;
import helpers.ConnectionProvider;
import models.Rating;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;

public class RatingLikeService {
    private final RatingLikeDAO ratingLikeDAO;
    private final ConnectionProvider connectionProvider;
    private final RatingDAO ratingDAO;
    private final MediaDAO mediaDAO;
    private final UserDAO userDAO;
    public RatingLikeService(UserDAO userDAO, MediaDAO mediaDAO, RatingDAO ratingDAO, RatingLikeDAO ratingLikeDAO, ConnectionProvider connectionProvider){
        this.ratingLikeDAO = ratingLikeDAO;
        this.ratingDAO = ratingDAO;
        this.mediaDAO = mediaDAO;
        this.userDAO = userDAO;
        this.connectionProvider = connectionProvider;
    }
    public RatingResponse likeRating(int rating_id, String likerUsername) throws SQLException {
        RatingResponse response = new RatingResponse();
        try(Connection con = connectionProvider.getConnection()){
            con.setAutoCommit(false);
            response = validateRequest(con, rating_id, likerUsername);
            if(response.getStatus() != 200 ){
                return response;
            }
            User liker = userDAO.findByUsername(con, likerUsername);
            response = ratingLikeDAO.likeRating(con, liker.getUserID(), rating_id);
            if(response.getStatus() == 200){
                con.commit();
            }else{
                con.rollback();
            }
            return response;
        }catch (SQLException e){
            response.setMessage("Internal server error: "+ e.getMessage());
            response.setStatus(500);
            throw e;
        }

    }
    public RatingResponse deleteLike(int like_id, String likerUsername) throws SQLException {
        RatingResponse response = new RatingResponse();
        try(Connection con = connectionProvider.getConnection()){
            con.setAutoCommit(false);

            Integer ownerId = ratingLikeDAO.getLikeOwner(con, like_id);
            if(ownerId == null){
                response.setStatus(404);
                response.setMessage("Like not found");
                con.rollback();
                return response;
            }

            // Check authorization
            User liker = userDAO.findByUsername(con, likerUsername);
            if(ownerId != liker.getUserID()){
                response.setStatus(403);
                response.setMessage("Forbidden: You can only delete your own likes");
                con.rollback();
                return response;
            }

            // Delete the like
            response = ratingLikeDAO.deleteLike(con, like_id);

            if(response.getStatus() == 200){
                con.commit();
            }else{
                con.rollback();
            }
        }catch (SQLException e){
            response.setStatus(500);
            response.setMessage("Internal server error: "+ e.getMessage());
            throw e;

        }
        return response;
    }

    private RatingResponse validateRequest(Connection con, int rating_id, String likerUsername) throws SQLException {
        RatingResponse response = new RatingResponse();
        Rating rating = ratingDAO.findById(con, rating_id);
        if(rating == null){
            response.setStatus(404);
            response.setMessage("Rating not found.");
            con.rollback();
            return response;
        }
        MediaRequest media = mediaDAO.findById(con, rating.getMediaID());

        if(media == null){
            response.setStatus(500);
            response.setMessage("No media associated with this rating");
            con.rollback();
            return response;
        }

        if(media.getCreatorUsername().equals(likerUsername)){
            response.setStatus(403);
            response.setMessage("Forbidden");
            con.rollback();
            return response;
        }
        response.setStatus(200);
        return response;
    }
}
