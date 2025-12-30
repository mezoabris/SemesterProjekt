package dataaccess;

import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import models.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    public List<RatingRequest> getMediaRatings(Connection con, int mediaID) throws SQLException {
        List<RatingRequest> ratings = new ArrayList<>();

        String sql = "SELECT r.*, m.* "
                + "FROM ratings r "
                + "INNER JOIN media_entries m ON r.media_id = m.id "
                + "INNER JOIN users u ON r.user_id = u.id "
                + "WHERE r.media_id = ? AND r.comment_approved = TRUE";
        try (PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, mediaID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String title = rs.getString("title");
                int stars = rs.getInt("stars");
                String comment = rs.getString("comment");
                java.sql.Timestamp createdAt = rs.getTimestamp("created_at");

                RatingRequest rating = new RatingRequest(title, stars, comment, createdAt);

                ratings.add(rating);

            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());

        }
        return ratings;
    }

    public RatingResponse createRating(Connection con, int userID, Rating ratingRequest) {
        String sql = "INSERT INTO ratings (media_id, user_id, stars, comment) VALUES (?, ?, ?, ?)";
        RatingResponse response = new RatingResponse();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, ratingRequest.getMediaID());
            stmt.setInt(2, userID);
            stmt.setInt(3, ratingRequest.getStars());
            stmt.setString(4, ratingRequest.getComment());
            int affected = stmt.executeUpdate();
            if(affected > 0){
                response.setStatus(200);
                response.setMessage("Successfully created rating");
            }else{
                response.setStatus(400);
                response.setMessage("Rating could not be created. Please check your input");
            }


        }catch(SQLException e){
            response.setStatus(500);
            response.setMessage("Failed to create rating");
            e.printStackTrace();
        }
        return response;
    }

    public RatingResponse updateRating(Connection con, int mediaID, Rating ratingRequest, int userID) {
        String SQL = "UPDATE ratings SET stars = ?, comment = ?, updated_at = CURRENT_TIMESTAMP WHERE media_id = ? AND user_id = ?";
        RatingResponse response = new RatingResponse();
        try(PreparedStatement stmt = con.prepareStatement(SQL)){
            stmt.setInt(1, ratingRequest.getStars());
            stmt.setString(2, ratingRequest.getComment());
            stmt.setInt(3, mediaID);
            stmt.setInt(4, userID);

            int affected = stmt.executeUpdate();
            if(affected > 0){
                response.setStatus(200);
                response.setMessage("Successfully updated rating");
            } else {
                response.setStatus(404);
                response.setMessage("Rating not found");
            }
        }catch(SQLException e){
            response.setStatus(500);
            response.setMessage("Failed to update rating");
            e.printStackTrace();
        }
        return response;
    }

    public Rating findByUserAndMedia(Connection con, int userID, int mediaID) {
        String SQL = "SELECT * FROM ratings WHERE user_id = ? AND media_id = ?";
        Rating rating = new Rating();
        try(PreparedStatement stmt = con.prepareStatement(SQL)){
            stmt.setInt(1, userID);
            stmt.setInt(2, mediaID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                rating.setUserID(rs.getInt("user_id"));
                rating.setMediaID(rs.getInt("media_id"));
                rating.setStars(rs.getInt("stars"));
                rating.setComment(rs.getString("comment"));

            }else{
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rating;
    }
    public Rating findById(Connection con, int ratingID) throws SQLException {
        String sql = "SELECT * FROM ratings WHERE id = ?";
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, ratingID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Rating rating = new Rating();
                rating.setUserID(rs.getInt("user_id"));
                rating.setMediaID(rs.getInt("media_id"));
                rating.setStars(rs.getInt("stars"));
                rating.setComment(rs.getString("comment"));
                rating.setCommentApproved(rs.getBoolean("comment_approved"));
                rating.setCreatedAt(rs.getTimestamp("created_at"));
                rating.setUpdatedAt(rs.getTimestamp("updated_at"));
                return rating;
            }
        }
        return null;
    }

    public RatingResponse approveComment(Connection con, int ratingID) throws SQLException {
        String sql = "UPDATE ratings SET comment_approved = true WHERE id = ?";
        RatingResponse res = new RatingResponse();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, ratingID);
            int effected = stmt.executeUpdate();
            if(effected > 0){
                res.setMessage("Comment approved successfully");
                res.setStatus(200);
            }else{
                res.setMessage("There has been an error approving this comment. Please try again");
                res.setStatus(400);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return res;
    }
    public RatingResponse deleteRatingById(Connection con, int ratingID) {
        String sql = "DELETE FROM ratings WHERE id = ?";
        RatingResponse response = new RatingResponse();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, ratingID);
            int affected = stmt.executeUpdate();
            if(affected > 0){
                response.setStatus(200);
                response.setMessage("Successfully deleted rating");
            }else{
                response.setStatus(404);
                response.setMessage("Rating not found");
            }
        }catch(SQLException e){
            response.setStatus(500);
            response.setMessage("Failed to delete rating");
            e.printStackTrace();
        }
        return response;
    }

    // Get all ratings by a specific user
    public List<Rating> getRatingsByUser(Connection con, int userID) throws SQLException {
        String sql = "SELECT * FROM ratings WHERE user_id = ?";
        List<Rating> ratings = new ArrayList<>();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Rating rating = new Rating();
                rating.setMediaID(rs.getInt("media_id"));
                rating.setUserID(rs.getInt("user_id"));
                rating.setStars(rs.getInt("stars"));
                rating.setComment(rs.getString("comment"));
                ratings.add(rating);
            }
        }
        return ratings;
    }

    // Get genres from media that a user rated highly (4+ stars)
    public List<String> getHighlyRatedGenresByUser(Connection con, int userID) throws SQLException {
        String sql = "SELECT DISTINCT UNNEST(m.genre) as genre " +
                     "FROM ratings r " +
                     "JOIN media_entries m ON r.media_id = m.id " +
                     "WHERE r.user_id = ? AND r.stars >= 4";
        List<String> genres = new ArrayList<>();
        try(PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                genres.add(rs.getString("genre"));
            }
        }
        return genres;
    }
}
