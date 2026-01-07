package dataaccess;

import datatransfer.FavoriteResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class FavoriteDAOStub extends FavoriteDAO {
    private final Map<Integer, Set<Integer>> userFavorites = new HashMap<>();

    public FavoriteDAOStub() {
        // User 1 has favorites: media 1, 2
        userFavorites.put(1, new HashSet<>(Arrays.asList(1, 2)));
        // User 2 has favorite: media 1
        userFavorites.put(2, new HashSet<>(Arrays.asList(1)));
    }

    @Override
    public FavoriteResponse addFavorite(Connection con, int userID, int mediaID) throws SQLException {
        FavoriteResponse response = new FavoriteResponse();

        if (!userFavorites.containsKey(userID)) {
            userFavorites.put(userID, new HashSet<>());
        }

        Set<Integer> favorites = userFavorites.get(userID);
        if (favorites.contains(mediaID)) {
            response.setStatus(409);
            response.setMessage("Already in favorites");
        } else {
            favorites.add(mediaID);
            response.setStatus(200);
            response.setMessage("Added to favorites");
        }
        return response;
    }

    @Override
    public FavoriteResponse removeFavorite(Connection con, int userID, int mediaID) throws SQLException {
        FavoriteResponse response = new FavoriteResponse();

        if (!userFavorites.containsKey(userID) || !userFavorites.get(userID).contains(mediaID)) {
            response.setStatus(404);
            response.setMessage("Not in favorites");
        } else {
            userFavorites.get(userID).remove(mediaID);
            response.setStatus(200);
            response.setMessage("Removed from favorites");
        }
        return response;
    }

    @Override
    public List<Integer> getFavoriteMediaIdsByUser(Connection con, int userID) throws SQLException {
        if (!userFavorites.containsKey(userID)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userFavorites.get(userID));
    }
}
