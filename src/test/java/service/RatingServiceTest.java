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
    private RatingDAOStub ratingStub;

    @BeforeEach
    void setup() throws SQLException {
        ratingStub = new RatingDAOStub();
        MediaDAOStub mediaStub = new MediaDAOStub();
        ConnectionProvider provider = mock(ConnectionProvider.class);
        when(provider.getConnection()).thenReturn(mock(Connection.class));

        ratingService = new RatingService(ratingStub, mediaStub, provider);
    }

    @Test
    void testCreateRating_success() throws SQLException {
        Rating newRating = new Rating(4, "Nice movie", new Timestamp(System.currentTimeMillis()));
        newRating.setMediaID(1);

        RatingResponse response = ratingService.createRating(1, newRating);

        assertEquals(200, response.getStatus());
        assertEquals("Successfully created rating", response.getMessage());
    }

    @Test
    void testUpdateRating_success() throws SQLException {
        Rating updatedRating = new Rating(5, "Updated comment", new Timestamp(System.currentTimeMillis()));

        RatingResponse response = ratingService.updateRating(1, updatedRating, 1);

        assertEquals(200, response.getStatus());
        assertEquals("Successfully updated rating", response.getMessage());
    }

    @Test
    void testUpdateRating_notFound() throws SQLException {
        Rating updatedRating = new Rating(5, "Comment", new Timestamp(System.currentTimeMillis()));

        RatingResponse response = ratingService.updateRating(999, updatedRating, 1);

        assertEquals(404, response.getStatus());
    }

    @Test
    void testFindRatingByUserAndMedia_found() throws SQLException {
        Rating rating = ratingService.findRatingByUserAndMedia(1, 1);

        assertNotNull(rating);
        assertEquals(1, rating.getMediaID());
    }

    @Test
    void testFindRatingByUserAndMedia_notFound() throws SQLException {
        Rating rating = ratingService.findRatingByUserAndMedia(1, 999);

        assertNull(rating);
    }
}
