package dataaccess;

import datatransfer.MediaRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    private final MediaDAO mediaDAO = new MediaDAO();

    public boolean addFavorite(Connection con, int mediaId, int userId) throws SQLException {
        String sql = "INSERT INTO favorites (media_id, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, mediaId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean removeFavorite(Connection con, int mediaId, int userId) throws SQLException {
        String sql = "DELETE FROM favorites WHERE media_id = ? AND user_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, mediaId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<MediaRequest> findFavoritesByUserID(Connection con, int userID) throws SQLException {
        List<MediaRequest> favorites = new ArrayList<>();
        // Join with users table to match by user ID since favorites table only has username
        String sql = "SELECT m.* FROM media_entries m " +
                     "JOIN favorites f ON m.id = f.media_id " +
                     "JOIN users u ON f.user_id = u.id " +
                     "WHERE u.id = ?";
                     
        try (PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, userID); 
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                favorites.add(mediaDAO.mapResultSetToMediaRequest(rs));
            }
        }
        return favorites;
    }
}
