package dataaccess;

import datatransfer.MediaRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class FavoriteDAOStub extends FavoriteDAO {
    private final Map<Integer, Set<Integer>> userFavorites = new HashMap<>();

    public FavoriteDAOStub() {
        userFavorites.put(1, new HashSet<>(Arrays.asList(1, 2)));
        userFavorites.put(2, new HashSet<>(Arrays.asList(1)));
    }

    @Override
    public boolean addFavorite(Connection con, int mediaID, int userID) throws SQLException {
        if (!userFavorites.containsKey(userID)) {
            userFavorites.put(userID, new HashSet<>());
        }

        Set<Integer> favorites = userFavorites.get(userID);
        if (favorites.contains(mediaID)) {
            return false; // Already exists
        } else {
            favorites.add(mediaID);
            return true; // Successfully added
        }
    }

    @Override
    public boolean removeFavorite(Connection con, int mediaID, int userID) throws SQLException {
        if (!userFavorites.containsKey(userID) || !userFavorites.get(userID).contains(mediaID)) {
            return false; // Not found
        } else {
            userFavorites.get(userID).remove(mediaID);
            return true; // Successfully removed
        }
    }

    @Override
    public List<MediaRequest> findFavoritesByUserID(Connection con, int userID) throws SQLException {
        // Return empty list for stub - we only need this for the check in addFavorite
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getFavoriteMediaIdsByUser(Connection con, int userID) throws SQLException {
        if (!userFavorites.containsKey(userID)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userFavorites.get(userID));
    }
}
