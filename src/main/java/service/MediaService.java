package service;

import dataaccess.MediaDAO;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MediaService {
    MediaDAO mediaDAO = new MediaDAO();

    public MediaResponse getMedia(Integer mediaID, Map<String, String> params) throws SQLException {
        MediaResponse response = new MediaResponse();

        // Handle filtering by params (future implementation)
        if(!params.isEmpty()){
            List<MediaRequest> filteredMedia =  mediaDAO.findByFilter(params);
            if(filteredMedia.isEmpty()){
                response.setStatus(200);
                response.setMessage("No media found");
            }
            else{
                response.setStatus(200);
                response.setMessage("Media found");
                response.setRequests(filteredMedia);
            }
            return response;
        }

        // Get specific media by ID
        if(mediaID != null) {
            MediaRequest media = mediaDAO.findById(mediaID);
            if (media != null) {
                response.setRequests(List.of(media));
                response.setStatus(200);
                response.setMessage("Media found");
            } else {
                response.setStatus(404);
                response.setMessage("No media found");
            }
        } else {
            // Get all media
            List<MediaRequest> allMedia = mediaDAO.findAll();
            response.setRequests(allMedia);
            response.setStatus(200);
            response.setMessage(allMedia.isEmpty() ? "No media found" : "Media found");
        }
        return response;
    }
    public MediaResponse upsertMedia(MediaRequest request, String[] segments) throws SQLException {
        validateRequest(request);
        Integer mediaID = extractMediaID(segments);

        MediaResponse response;
        if (mediaID == null) {
            response = createMedia(request);
        } else {
            response = updateMediaByID(mediaID, request);
        }
        return response;

    }

    public MediaResponse deleteMediaByID(Integer mediaID) {
        return mediaDAO.deleteMedia(mediaID);
    }

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


    public MediaResponse updateMediaByID(Integer mediaID, MediaRequest request) throws SQLException {
        validateRequest(request);
        MediaResponse response = mediaDAO.updateMedia(mediaID, request);
        return response;
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
    private Integer extractMediaID(String[] segments) {
        if (segments == null || segments.length <= 3) {
            return null; // no ID provided → treat as create
        }
        String idStr = segments[3];
        if (idStr == null || idStr.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(idStr); // prefer valueOf over parseInt for Integer
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Media ID must be a number"); // or throw if you consider malformed ID an error
        }
    }
    private List<String> parseGenres(String genreParam) {
        if (genreParam == null || genreParam.isBlank()) return new ArrayList<>();
        return Arrays.stream(genreParam.split(","))
                .map(String::trim)
                .toList();
    }

    private static Integer parseIntSafe(Map<String, String> params, String key) {
        try {
            return params.containsKey(key) ? Integer.parseInt(params.get(key)) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}



