package dataaccess;

import datatransfer.MediaRequest;
import datatransfer.MediaResponse;
import models.MediaEntry;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MediaDAOStub extends MediaDAO {
    private final List<MediaRequest> mediaList = new ArrayList<>();
    private int nextId = 1;

    public MediaDAOStub() {
        MediaRequest media1 = new MediaRequest(1, "Inception", "Dream heist", "movie", 2010, Arrays.asList("sci-fi", "action"), 13);
        media1.setMediaId(nextId++);
        media1.setCreatorUsername("test1");

        MediaRequest media2 = new MediaRequest(1, "Breaking Bad", "Chemistry teacher", "series", 2008, Arrays.asList("drama", "crime"), 18);
        media2.setMediaId(nextId++);
        media2.setCreatorUsername("test1");

        mediaList.add(media1);
        mediaList.add(media2);
    }

    @Override
    public boolean createMedia(Connection con, MediaRequest media) throws SQLException {
        media.setMediaId(nextId++);
        mediaList.add(media);
        return true;
    }

    @Override
    public MediaRequest findById(Connection con, Integer mediaID) throws SQLException {
        for (MediaRequest media : mediaList) {
            if (media.getMediaId().equals(mediaID)) {
                return media;
            }
        }
        return null;
    }

    @Override
    public List<MediaRequest> findAll(Connection con) throws SQLException {
        return new ArrayList<>(mediaList);
    }

    @Override
    public MediaResponse updateMedia(Connection con, int mediaID, MediaRequest media) throws SQLException {
        MediaResponse response = new MediaResponse();
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMediaId().equals(mediaID)) {
                media.setMediaId(mediaID);
                mediaList.set(i, media);
                response.setStatus(200);
                response.setMessage("Successfully updated media");
                return response;
            }
        }
        response.setStatus(404);
        response.setMessage("Media not found");
        return response;
    }

    @Override
    public MediaResponse deleteMedia(Connection con, int mediaID) {
        MediaResponse response = new MediaResponse();
        for (int i = 0; i < mediaList.size(); i++) {
            if (mediaList.get(i).getMediaId().equals(mediaID)) {
                mediaList.remove(i);
                response.setStatus(200);
                response.setMessage("Successfully deleted media");
                return response;
            }
        }
        response.setStatus(404);
        response.setMessage("Media not found");
        return response;
    }
}
