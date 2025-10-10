package dataaccess;

import config.DatabaseConfig;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MediaDAO {


    public boolean createMedia(MediaRequest media) throws SQLException {
        String sql = "INSERT INTO media_entries(title, description, media_type, release_year, genre, age_restriction, creator) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, media.getTitle());
            stmt.setString(2, media.getDescription());
            stmt.setString(3, media.getMediaType());
            stmt.setInt(4, media.getReleaseYear());
            stmt.setArray(5, conn.createArrayOf("text", media.getGenres().toArray()));
            stmt.setInt(6, media.getAgeRestriction());
            stmt.setString(7, media.getCreator());

            return stmt.executeUpdate() > 0;
        }
    }

    private MediaRequest mapResultSetToMediaRequest(ResultSet rs) throws SQLException {
        MediaRequest media = new MediaRequest();
        media.setTitle(rs.getString("title"));
        media.setDescription(rs.getString("description"));
        media.setMediaType(rs.getString("media_type"));
        media.setReleaseYear(rs.getInt("release_year"));

        Array sqlArray = rs.getArray("genre");
        if (sqlArray != null) {
            String[] genreArray = (String[]) sqlArray.getArray();
            media.setGenres(Arrays.asList(genreArray));
        } else {
            media.setGenres(Collections.emptyList());
        }

        media.setAgeRestriction(rs.getInt("age_restriction"));
        media.setCreator(rs.getString("creator"));

        return media;
    }

    public MediaRequest findById(Integer mediaID) throws SQLException {
        String sql = "SELECT * FROM media_entries WHERE id = ?";
        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, mediaID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMediaRequest(rs);
            } else {
                return null;
            }
        }
    }
    public MediaResponse updateMedia(int mediaID,MediaRequest media) throws SQLException {
        String title= media.getTitle();
        String description = media.getDescription();
        String mediaType = media.getMediaType();
        int releaseYear = media.getReleaseYear();
        List<String> genre = media.getGenres();
        int ageRestriction = media.getAgeRestriction();
        String creator = media.getCreator();
        MediaResponse response = new MediaResponse();
        String sql = "UPDATE media_entries SET title = ?, description = ?, " +
                                             "media_type = ?, release_year = ?, " +
                                             "genre = ?, age_restriction = ?, " +
                                             "updated_at = ? WHERE  id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, mediaType);
            stmt.setInt(4, releaseYear);
            stmt.setArray(5, conn.createArrayOf("text", genre.toArray()));
            stmt.setInt(6, ageRestriction);
            stmt.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setInt(8, mediaID);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                response.setStatus(200);
                response.setMessage("Successfully updated media");
            }else{
                response.setStatus(500);
                response.setMessage("Failed to update media");
            }
            return response;


        }
    }

    public List<MediaRequest> findAll() throws SQLException {
        String sql = "SELECT * FROM media_entries";
        List<MediaRequest> results = new ArrayList<>();

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapResultSetToMediaRequest(rs));
            }
        }

        return results;
    }
}
