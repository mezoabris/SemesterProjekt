package service;

import dataaccess.FavoriteDAOStub;
import dataaccess.MediaDAOStub;
import datatransfer.FavoriteResponse;
import helpers.ConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @BeforeEach
    void setup() throws SQLException {
        FavoriteDAOStub favoriteStub = new FavoriteDAOStub();
        MediaDAOStub mediaStub = new MediaDAOStub();
        ConnectionProvider provider = mock(ConnectionProvider.class);
        when(provider.getConnection()).thenReturn(mock(Connection.class));

        favoriteService = new FavoriteService(favoriteStub, mediaStub, provider);
    }

    @Test
    void testAddFavorite_success() throws SQLException {
        FavoriteResponse response = favoriteService.addFavorite(1, 3);

        assertEquals(200, response.getStatus());
        assertEquals("Added to favorites", response.getMessage());
    }

    @Test
    void testAddFavorite_alreadyExists() throws SQLException {
        FavoriteResponse response = favoriteService.addFavorite(1, 1);

        assertEquals(409, response.getStatus());
        assertEquals("Already in favorites", response.getMessage());
    }

    @Test
    void testRemoveFavorite_success() throws SQLException {
        FavoriteResponse response = favoriteService.removeFavorite(1, 1);

        assertEquals(200, response.getStatus());
        assertEquals("Removed from favorites", response.getMessage());
    }

    @Test
    void testRemoveFavorite_notFound() throws SQLException {
        FavoriteResponse response = favoriteService.removeFavorite(1, 999);

        assertEquals(404, response.getStatus());
        assertEquals("Not in favorites", response.getMessage());
    }
}
