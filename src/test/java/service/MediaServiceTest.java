package service;

import dataaccess.MediaDAOStub;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;
import helpers.ConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MediaServiceTest {
    private MediaService mediaService;

    @BeforeEach
    void setup() throws SQLException {
        MediaDAOStub mediaStub = new MediaDAOStub();
        ConnectionProvider provider = mock(ConnectionProvider.class);
        when(provider.getConnection()).thenReturn(mock(Connection.class));

        mediaService = new MediaService(mediaStub, provider);
    }

    @Test
    void testCreateMedia_success() {
        MediaRequest newMedia = new MediaRequest(1, "The Matrix", "Hacker discovers reality", "movie", 1999, Arrays.asList("sci-fi", "action"), 16);
        newMedia.setCreatorUsername("test1");

        MediaResponse response = mediaService.createMedia(newMedia);

        assertEquals(200, response.getStatus());
        assertEquals("Media created successfully", response.getMessage());
    }

    @Test
    void testGetMediaById_found() throws SQLException {
        MediaResponse response = mediaService.getMedia(1, new HashMap<>());

        assertEquals(200, response.getStatus());
        assertEquals("Media found", response.getMessage());
        assertNotNull(response.getRequests());
        assertEquals(1, response.getRequests().size());
        assertEquals("Inception", response.getRequests().getFirst().getTitle());
    }

    @Test
    void testGetMediaById_notFound() throws SQLException {
        MediaResponse response = mediaService.getMedia(999, new HashMap<>());

        assertEquals(404, response.getStatus());
        assertEquals("No media found", response.getMessage());
    }

    @Test
    void testGetAllMedia() throws SQLException {
        MediaResponse response = mediaService.getMedia(null, new HashMap<>());

        assertEquals(200, response.getStatus());
        assertNotNull(response.getRequests());
        assertEquals(2, response.getRequests().size());
    }

    @Test
    void testUpdateMedia_success() throws SQLException {
        MediaRequest updatedMedia = new MediaRequest(1, "Inception Updated", "Updated description", "movie", 2010, List.of("sci-fi"), 13);
        updatedMedia.setCreatorUsername("test1");

        MediaResponse response = mediaService.updateMediaByID(1, updatedMedia, "test1");

        assertEquals(200, response.getStatus());
        assertEquals("Successfully updated media", response.getMessage());
    }

    @Test
    void testUpdateMedia_notFound() throws SQLException {
        MediaRequest updatedMedia = new MediaRequest(1, "Test", "Test", "movie", 2020, List.of("drama"), 12);

        MediaResponse response = mediaService.updateMediaByID(999, updatedMedia, "test1");

        assertEquals(404, response.getStatus());
        assertEquals("Media not found!", response.getMessage());
    }

    @Test
    void testDeleteMedia_success() throws SQLException {
        MediaResponse response = mediaService.deleteMediaByID(1, "test1");

        assertEquals(200, response.getStatus());
        assertEquals("Successfully deleted media", response.getMessage());
    }

    @Test
    void testDeleteMedia_notFound() throws SQLException {
        MediaResponse response = mediaService.deleteMediaByID(999, "test1");

        assertEquals(404, response.getStatus());
        assertEquals("Media not found!", response.getMessage());
    }
}
