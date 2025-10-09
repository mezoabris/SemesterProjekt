package dataaccess;

import config.DatabaseConfig;
import datatransfer.RatingRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingDAO {
    public List<RatingRequest> getUserRatings(int userID) throws SQLException {
        List<RatingRequest> ratings = new ArrayList<>();

        String sql = "SELECT r.*, m.* "
                + "FROM ratings r "
                + "INNER JOIN media_entries m ON r.media_id = m.id "
                + "INNER JOIN users u ON r.user_id = u.id "
                + "WHERE r.user_id = ?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, userID);
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
}
