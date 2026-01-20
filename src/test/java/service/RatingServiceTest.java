package service;

import dataaccess.MediaDAOStub;
import dataaccess.RatingDAOStub;
import datatransfer.RatingResponse;
import helpers.ConnectionProvider;
import models.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RatingServiceTest {
    private RatingService ratingService;

    @BeforeEach
    void setup() throws SQLException {
        RatingDAOStub ratingStub = new RatingDAOStub();
        MediaDAOStub mediaStub = new MediaDAOStub();
        ConnectionProvider provider = mock(ConnectionProvider.class);
        when(provider.getConnection()).thenReturn(mock(Connection.class));

        ratingService = new RatingService(ratingStub, mediaStub, provider);
    }

    @Test
    void testSaveRating_createNew() throws SQLException {
        Rating newRating = new Rating(4, "Nice movie", new Timestamp(System.currentTimeMillis()));
        newRating.setMediaID(3);

        RatingResponse response = ratingService.saveRating(1, newRating);

        assertEquals(200, response.getStatus());
        assertEquals("Successfully created rating", response.getMessage());
    }

    @Test
    void testSaveRating_updateExisting() throws SQLException {
        Rating updatedRating = new Rating(5, "Updated comment", new Timestamp(System.currentTimeMillis()));
        updatedRating.setMediaID(1);

        RatingResponse response = ratingService.saveRating(1, updatedRating);

        assertEquals(200, response.getStatus());
        assertEquals("Successfully updated rating", response.getMessage());
    }

    @Test
    void testApproveRating_success() throws SQLException {
        RatingResponse response = ratingService.approveRating(1, "test1");

        assertEquals(200, response.getStatus());
    }

    @Test
    void testRemoveRating_success() throws SQLException {
        RatingResponse response = ratingService.removeRating(1, 1);

        assertEquals(200, response.getStatus());
    }
}
