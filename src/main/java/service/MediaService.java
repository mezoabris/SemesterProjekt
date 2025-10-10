package service;

import dataaccess.MediaDAO;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MediaService {
    MediaDAO mediaDAO = new MediaDAO();
    public MediaResponse createMedia(MediaRequest request) {
        validateRequest(request);
        MediaResponse response = new MediaResponse();
        try {
            boolean success = mediaDAO.createMedia(request);
            if (success) {
                response.setStatus(200);
                response.setMessage("Media created successfully");
            } else {
                response.setStatus(500);
                response.setMessage("Failed to create media");
            }
        } catch (SQLException e) {
            response.setStatus(400);
            response.setMessage("Database error: " + e.getMessage());
        }
        return response;
    }

    public MediaResponse getMediaByID(Integer mediaID) {
        MediaResponse response = new MediaResponse();
        try {
            MediaRequest media = mediaDAO.findById(mediaID);
            if (media != null) {
                response.setRequests(List.of(media));
                response.setStatus(200);
                response.setMessage("Media found");
            } else {
                response.setStatus(404);
                response.setMessage("No media found");
            }
        } catch (SQLException e) {
            response.setStatus(400);
            response.setMessage("Database error: " + e.getMessage());
        }
        return response;
    }

    public MediaResponse getAllMedia() {
        MediaResponse response = new MediaResponse();
        try {
            List<MediaRequest> mediaList = mediaDAO.findAll();
            response.setRequests(mediaList);
            response.setStatus(200);
            response.setMessage(mediaList.isEmpty() ? "No media found" : "Media found");
        } catch (SQLException e) {
            response.setStatus(400);
            response.setMessage("Database error: " + e.getMessage());
        }
        return response;
    }
    public MediaResponse updateMediaByID(Integer mediaID, MediaRequest request) throws SQLException {
        validateRequest(request);
        MediaResponse response =mediaDAO.updateMedia(mediaID, request);
        return response;
    }
    public MediaResponse deleteMediaByID(Integer mediaID) {
        MediaResponse response = mediaDAO.deleteMedia(mediaID);
        return response;

    }

    public List<MediaResponse> searchMedia(String title, List<String> genres, String mediaType, Integer releaseYear, Integer ageRestriction, Integer rating, String sortBy) {
        List<MediaResponse> mediaResponses = new ArrayList<>();
        return mediaResponses;
    }
    private void validateRequest(MediaRequest request) {
        if (isNullOrBlank(request.getCreator(), request.getTitle(),
                request.getDescription(), request.getMediaType())) {
            throw new IllegalArgumentException("Missing required string fields");
        }


        if (request.getReleaseYear() < 1888 || request.getReleaseYear() > LocalDate.now().getYear()) {
            throw new IllegalArgumentException("Invalid release year");
        }


        if (request.getGenres() == null || request.getGenres().isEmpty()) {
            throw new IllegalArgumentException("At least one genre is required");
        }


        if (request.getAgeRestriction() < 0 || request.getAgeRestriction() > 21) {
            throw new IllegalArgumentException("Invalid age restriction");
        }


    }
    private boolean isNullOrBlank(String... values) {
        for (String v : values) {
            if (v == null || v.isBlank()) return true;
        }
        return false;
    }


}
