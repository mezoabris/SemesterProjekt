package service;

import dataaccess.FavoriteDAO;
import dataaccess.MediaDAO;
import dataaccess.RatingDAO;
import datatransfer.MediaRequest;
import datatransfer.RecommendationResponse;
import helpers.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationService {
    private final RatingDAO ratingDAO;
    private final FavoriteDAO favoriteDAO;
    private final MediaDAO mediaDAO;
    private final ConnectionProvider connectionProvider;

    public RecommendationService(RatingDAO ratingDAO, FavoriteDAO favoriteDAO, MediaDAO mediaDAO, ConnectionProvider connectionProvider){
        this.ratingDAO = ratingDAO;
        this.favoriteDAO = favoriteDAO;
        this.mediaDAO = mediaDAO;
        this.connectionProvider = connectionProvider;
    }

    public RecommendationResponse getRecommendations(int userID) {
        Connection con = null;
        try {
            con = connectionProvider.getConnection();

            List<String> preferredGenres = ratingDAO.getHighlyRatedGenresByUser(con, userID);

            if (preferredGenres.isEmpty()) {
                return new RecommendationResponse(200, "No recommendations available yet. Rate some media to get personalized recommendations!", new ArrayList<>());
            }

            List<MediaRequest> candidateMedia = mediaDAO.findByGenres(con, preferredGenres);

            Set<Integer> ratedMediaIds = ratingDAO.getRatingsByUser(con, userID).stream()
                    .map(rating -> rating.getMediaID())
                    .collect(Collectors.toSet());

            Set<Integer> favoriteMediaIds = new HashSet<>(favoriteDAO.getFavoriteMediaIdsByUser(con, userID));

            List<MediaRequest> recommendations = candidateMedia.stream()
                    .filter(media -> !ratedMediaIds.contains(getMediaId(media)))
                    .filter(media -> !favoriteMediaIds.contains(getMediaId(media)))
                    .limit(10)
                    .collect(Collectors.toList());

            if (recommendations.isEmpty()) {
                return new RecommendationResponse(200, "You've already explored all recommendations in your preferred genres!", new ArrayList<>());
            }

            return new RecommendationResponse(200, "Recommendations based on your ratings", recommendations);

        } catch (SQLException e) {
            e.printStackTrace();
            return new RecommendationResponse(500, "Error generating recommendations: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Integer getMediaId(MediaRequest media) {
        return media.getMediaId();
    }
}
