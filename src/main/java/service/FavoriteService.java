package service;

import dataaccess.FavoriteDAO;
import dataaccess.MediaDAO;
import datatransfer.MediaResponse;
import datatransfer.MediaRequest; // For the list of favorites
import datatransfer.FavoritesRequest; // If you still want to wrap the list
import helpers.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class FavoriteService {
    private final FavoriteDAO favoriteDAO;
    private final MediaDAO mediaDAO;
    private final ConnectionProvider connectionProvider;

    public FavoriteService(FavoriteDAO favoriteDAO, MediaDAO mediaDAO, ConnectionProvider connectionProvider) {
        this.favoriteDAO = favoriteDAO;
        this.mediaDAO = mediaDAO;
        this.connectionProvider = connectionProvider;
    }

    public List<MediaRequest> getFavoritesByUserId(int userId) throws SQLException {
        try (Connection conn = connectionProvider.getConnection()) {
             return favoriteDAO.findFavoritesByUserID(conn, userId);
        }
    }

    public MediaResponse addFavorite(int mediaId, int userId) throws SQLException {
        MediaResponse response = new MediaResponse();
        Connection conn = null;
        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);
            MediaRequest media = mediaDAO.findById(conn, mediaId);
            if (media == null) {
                response.setStatus(404);
                response.setMessage("Media not found");
                conn.rollback();
                return response;
            }
            List<MediaRequest> currentFavorites = favoriteDAO.findFavoritesByUserID(conn, userId);
            for(MediaRequest favorite: currentFavorites){
                if(Objects.equals(media.getTitle(), favorite.getTitle())){
                    response.setStatus(200);
                    response.setMessage("Media already marked as favorite");
                    conn.rollback();
                    return response;
                }
            }

            boolean added = favoriteDAO.addFavorite(conn, mediaId, userId);
            if (added) {
                response.setStatus(200);
                response.setMessage("Added to favorites");
                conn.commit();
            } else {
                response.setStatus(409);
                response.setMessage("Already in favorites");
                conn.rollback();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            if (e.getMessage().contains("favorites_media_id_username_key")) {
                 response.setStatus(409);
                 response.setMessage("Already in favorites");
            } else {
                 throw e;
            }
        } finally {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return response;
    }

    public MediaResponse removeFavorite(int mediaId, int userId) throws SQLException {
        MediaResponse response = new MediaResponse();
        Connection conn = null;
        try {
            conn = connectionProvider.getConnection();
            conn.setAutoCommit(false);
            

            
            boolean removed = favoriteDAO.removeFavorite(conn, mediaId, userId);
            if (removed) {
                response.setStatus(200);
                response.setMessage("Removed from favorites");
                conn.commit();
            } else {
                response.setStatus(404);
                response.setMessage("Favorite not found");
                conn.rollback();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    if (!conn.isClosed()) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return response;
    }
}
