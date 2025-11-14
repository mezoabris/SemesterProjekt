package dataaccess;

import config.DatabaseConfig;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;

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

    public MediaRequest mapResultSetToMediaRequest(ResultSet rs) throws SQLException {
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
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
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
    public MediaResponse deleteMedia(int mediaID){
        MediaResponse response = new MediaResponse();
        String sql = "DELETE FROM media_entries WHERE id = ?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mediaID);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                response.setStatus(200);
                response.setMessage("Successfully deleted media");
            }else{
                response.setStatus(400);
                response.setMessage("Failed to delete media");
            }

        }catch (SQLException e){
            response.setStatus(500);
            response.setMessage("Failed to delete media");
        }
        return response;
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
    public List<MediaRequest> findByFilter(Map<String, String> params) throws SQLException {
        List<Object> paramValues = new ArrayList<>();
        StringBuilder sql = makeQueryHelper(params, paramValues);

        try (Connection con = DatabaseConfig.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql.toString())) {
            for (int j = 0; j < paramValues.size(); j++) {
                Object val = paramValues.get(j);
                switch (val) {
                    case Integer integer -> stmt.setInt(j + 1, integer);
                    case String[] strings -> stmt.setArray(j + 1, con.createArrayOf("text", strings));
                    case String s -> stmt.setString(j + 1, s);
                    case null, default -> stmt.setObject(j + 1, val);
                }
            }

            ResultSet rs = stmt.executeQuery();
            List<MediaRequest> results = new ArrayList<>();
            while (rs.next()) {
                results.add(mapResultSetToMediaRequest(rs));
            }
            return results;
        }

    }
    public StringBuilder makeQueryHelper(Map<String, String> params, List<Object> paramValues) throws SQLException {
        Integer ratingFilter = null;


        StringBuilder sql = new StringBuilder(
                "SELECT m.*, AVG(r.stars) AS avg_rating " +
                        "FROM media_entries m " +
                        "LEFT JOIN ratings r ON m.id = r.media_id " +
                        "WHERE "
        );

        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("sortBy")) {
                continue;
            }
            if(key.equals("rating")) {
                ratingFilter = Integer.parseInt(value);
                continue;
            }
            if (i > 0) sql.append(" AND ");

            switch (key) {
                case "title":
                    sql.append("m.title ILIKE ?");
                    paramValues.add("%" + value + "%");
                    break;
                case "genre":
                    sql.append("m.genre @> ?");
                    paramValues.add(new String[]{value});
                    break;
                case "media_type":
                    sql.append("m.media_type = ?");
                    paramValues.add(value);
                    break;
                case "release_year":
                    sql.append("release_year >= ?");
                    paramValues.add(Integer.parseInt(value));
                    break;
                case "age_restriction":
                    sql.append("age_restriction >= ?");
                    paramValues.add(value);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown filter: " + key);
            }
            i++;
        }
        sql.append(" GROUP BY m.id");
        if(ratingFilter != null) {
            sql.append(" HAVING AVG(r.stars) >= ? ");
            paramValues.add(ratingFilter);
        }

        if (params.containsKey("sortBy")) {
            String sortColumn = params.get("sortBy");
            List<String> allowedSort = List.of("title", "release_year", "media_type", "age_restriction");
            if(!allowedSort.contains(sortColumn)) {
                throw new IllegalArgumentException("Invalid sort column: " + sortColumn);
            }
            sql.append(" ORDER BY ").append(sortColumn);
        }
        return sql;

    }
}
