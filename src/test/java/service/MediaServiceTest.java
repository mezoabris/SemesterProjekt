package service;

import dataaccess.MediaDAOStub;
import dataaccess.UserDAOStub;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;
import helpers.ConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MediaServiceTest {
    private MediaService mediaService;
    private MediaDAOStub mediaStub;

    @BeforeEach
    void setup() throws SQLException {
        mediaStub = new MediaDAOStub();
        UserDAOStub userStub = new UserDAOStub();
        ConnectionProvider provider = mock(ConnectionProvider.class);
        when(provider.getConnection()).thenReturn(mock(Connection.class));

        mediaService = new MediaService(mediaStub, userStub, provider);
    }

    @Test
    void testCreateMedia() throws SQLException {
        MediaRequest newMedia = new MediaRequest(1, "The Matrix", "Hacker discovers reality", "movie", 1999, Arrays.asList("sci-fi", "action"), 16);

        boolean result = mediaService.createMedia(newMedia);

        assertTrue(result);
    }

    @Test
    void testFindMediaById_found() throws SQLException {
        MediaRequest media = mediaService.getMedia(1);

        assertNotNull(media);
        assertEquals("Inception", media.getTitle());
    }

    @Test
    void testFindMediaById_notFound() throws SQLException {
        MediaRequest media = mediaService.getMedia(999);

        assertNull(media);
    }

    @Test
    void testUpdateMedia_success() throws SQLException {
        MediaRequest updatedMedia = new MediaRequest(1, "Inception Updated", "Updated description", "movie", 2010, Arrays.asList("sci-fi"), 13);

        MediaResponse response = mediaService.editMedia(1, updatedMedia);

        assertEquals(200, response.getStatus());
        assertEquals("Successfully updated media", response.getMessage());
    }

    @Test
    void testUpdateMedia_notFound() throws SQLException {
        MediaRequest updatedMedia = new MediaRequest(1, "Test", "Test", "movie", 2020, Arrays.asList("drama"), 12);

        MediaResponse response = mediaService.editMedia(999, updatedMedia);

        assertEquals(404, response.getStatus());
    }

    @Test
    void testDeleteMedia_success() throws SQLException {
        MediaResponse response = mediaService.deleteMedia(1, 1);

        assertEquals(200, response.getStatus());
        assertEquals("Successfully deleted media", response.getMessage());
    }

    @Test
    void testDeleteMedia_notFound() throws SQLException {
        MediaResponse response = mediaService.deleteMedia(999, 1);

        assertEquals(404, response.getStatus());
    }

    @Test
    void testGetAllMedia() throws SQLException {
        List<MediaRequest> allMedia = mediaService.getAllMedia();

        assertNotNull(allMedia);
        assertEquals(2, allMedia.size());
    }
}
