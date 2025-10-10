package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.MediaService;

import java.awt.geom.IllegalPathStateException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MediaHandler implements HttpHandler {
    private final MediaService mediaService =  new MediaService();
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = HttpHelper.getQueryParams(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");
        User user = TokenHelper.requireValidToken(exchange);
        if (user == null) return;

        try {
            switch (method) {
                case "GET" -> handleGet(exchange, segments, params);
                case "POST" -> handleWrite(exchange, user, null); // ✅ unified handler
                case "PUT" -> handleWrite(exchange, user, extractMediaID(segments));
                case "DELETE" -> handleDelete(exchange, segments);
                default -> HttpHelper.sendJSONResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpHelper.sendJSONResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }


    private void handleWrite(HttpExchange exchange, User user, Integer mediaID) throws IOException {
        MediaRequest request = HttpHelper.parseRequestBody(exchange, MediaRequest.class);
        request.setCreator(user.getUsername());
        try {
            MediaResponse response;
            if (mediaID == null) {
                response = mediaService.createMedia(request);
            } else {
                System.out.println("updating media ID: " + mediaID);
                response = mediaService.updateMediaByID(mediaID, request);
            }
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
        }catch (SQLException e){
            HttpHelper.sendJSONResponse(exchange, 500, "Internal server error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            HttpHelper.sendJSONResponse(exchange, 400, e.getMessage());
        } catch (RuntimeException e) {
            HttpHelper.sendJSONResponse(exchange, 500, e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange, String[] segments, Map<String, String> params) throws IOException {
        if (segments.length == 4) {
            Integer mediaID = extractMediaID(segments);
            MediaResponse response = mediaService.getMediaByID(mediaID);
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
            return;
        }
        if (segments.length == 3 && params.isEmpty()) {
            MediaResponse response = mediaService.getAllMedia();
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
            return;
        }

        // optional: filtered GET
        if (!params.isEmpty()) {
            var results = mediaService.searchMedia(
                    params.get("title"),
                    parseGenres(params.get("genre")),
                    params.get("mediaType"),
                    parseIntSafe(params, "releaseYear"),
                    parseIntSafe(params, "ageRestriction"),
                    parseIntSafe(params, "rating"),
                    params.get("sortBy")
            );
            HttpHelper.sendJSONResponse(exchange, 200, results);
            return;
        }

        HttpHelper.sendJSONResponse(exchange, 400, "Invalid request");
    }

    private void handleDelete(HttpExchange exchange, String[] segments) throws IOException {
        if (segments.length == 4) {
            Integer mediaID = extractMediaID(segments);
            MediaResponse response = mediaService.deleteMediaByID(mediaID);
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
        } else {
            HttpHelper.sendJSONResponse(exchange, 400, "Invalid request");
        }
    }

    private Integer extractMediaID(String[] segments) {
        try {
            return Integer.parseInt(segments[3]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid media ID");
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
    /*
    TODO createMediaEntry(MediaRequest req) POST
    TODO deleteMediaEntry() DEL
    TODO updateMediaEntry(MediaRequest req) PUT
    TODO getMedia() GET List media entries
    TODO getMediaById(int Id) GET
    TODO deleteMedia(int Id) DELETE
    TODO updateMedia(int Id) PUT
    TODO getFilteredMedia( filters ) GET
     */
}
