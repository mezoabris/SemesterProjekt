package dataaccess;

import config.DatabaseConfig;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MediaDAO {
    public MediaResponse createMedia(MediaRequest media){
        int status;
        String message;
        MediaResponse mediaResponse = new MediaResponse();
        String sql = "INSERT INTO media_entries(title, description, media_type, release_year, genre, age_restriction, creator) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, media.getTitle());
            stmt.setString(2, media.getDescription());
            stmt.setString(3, media.getMediaType());
            stmt.setInt(4, media.getReleaseYear());
            stmt.setArray(5, conn.createArrayOf("text", media.getGenres().toArray()));
            stmt.setInt(6, media.getAgeRestriction());
            stmt.setString(7, media.getCreator());
            int affected = stmt.executeUpdate();

            if (affected == 0){
                throw new SQLException("Insert failed");
            }else{
                status = 200;
                message = "Media created";
            }
            mediaResponse.setMessage(message);
            mediaResponse.setStatus(status);
            return mediaResponse;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
