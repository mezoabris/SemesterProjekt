package dataaccess;

import datatransfer.MediaResponse;
import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import models.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class RatingLikeDAO {
    public RatingResponse likeRating(Connection con, int user_id, int rating_id) throws SQLException {
        String sql = "INSERT INTO rating_likes (user_id, rating_id) VALUES(?, ?)";
        RatingResponse response = new RatingResponse();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, user_id);
            stmt.setInt(2, rating_id);
            int affected = stmt.executeUpdate();
            if(affected > 0){
                response.setStatus(200);
                response.setMessage("Successfully liked the rating");
            }else{
                response.setStatus(400);
                response.setMessage("Something has gone wrong while liking a rating. Please check your input");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return response;
    }
    public RatingResponse deleteLike(Connection con, int rating_id, int user_id) throws SQLException {
        String sql = "DELETE FROM rating_likes WHERE rating_id = ? AND user_id = ?";
        RatingResponse response = new RatingResponse();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, rating_id);
            stmt.setInt(2, user_id);
            int affected = stmt.executeUpdate();
            if(affected > 0){
                response.setStatus(200);
                response.setMessage("Successfully removed like from rating");
            }else{
                response.setStatus(404);
                response.setMessage("Like not found or already removed");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return response;
    }

}
