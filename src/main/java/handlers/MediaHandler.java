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
import java.util.*;
import java.util.stream.Collectors;

public class MediaHandler implements HttpHandler {
    MediaService mediaService =  new MediaService();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> params = HttpHelper.getQueryParams(exchange);
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");
        Integer mediaID = null;
        User user = TokenHelper.requireValidToken(exchange);
        if (user == null) return;

        switch (method) {
            case "GET":
                if(segments.length == 4){
                    mediaID = Integer.parseInt(segments[3]);
                    String converted = String.valueOf(mediaID);
                    MediaResponse response = mediaService.getMediaByID(mediaID);
                    HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);

                }
                if(segments.length == 3){
                    MediaResponse response = mediaService.getAllMedia();
                    HttpHelper.sendJSONResponse(exchange, response.getStatus(),  response);
                }
                break;
            case "POST":
                MediaRequest request = HttpHelper.parseRequestBody(exchange,  MediaRequest.class);
                request.setCreator(user.getUsername());
                try {
                    MediaResponse response = mediaService.createMedia(request);
                    HttpHelper.sendJSONResponse(exchange, 200, response);
                } catch (IllegalArgumentException e){
                    HttpHelper.sendJSONResponse(exchange, 400, e.getMessage());
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                    HttpHelper.sendJSONResponse(exchange, 500, e.getMessage());
                }


            case "PUT":
                break;
            case "DELETE":
                break;
        }
















        if (segments.length == 4) { // e.g., /api/media/123
            try {
                mediaID = Integer.parseInt(segments[3]);

            } catch (NumberFormatException e) {
                HttpHelper.sendJSONResponse(exchange, 400, "Invalid mediaID");
                return;
            }
        }
        if ("POST".equals(method) && mediaID == null) {
            MediaRequest request = HttpHelper.parseRequestBody(exchange, MediaRequest.class);
            request.setCreator(user.getUsername());
            mediaService.createMedia(request);
            HttpHelper.sendJSONResponse(exchange, 201, "Media created");
            return;
        }
        if ("GET".equals(method) && mediaID != null) {
            MediaResponse media = mediaService.getMediaByID(mediaID);
            if (media == null) {
                HttpHelper.sendJSONResponse(exchange, 404, "Media not found");
                return;
            }
            HttpHelper.sendJSONResponse(exchange, 200, media);
            return;
        }
        if("GET".equals(method) && !params.isEmpty()) {
            String title = params.get("title");
            String genreParam = params.get("genre");
            List<String> genres = genreParam == null ? new ArrayList<>() :
                    Arrays.stream(genreParam.split(","))
                            .map(String::trim)
                            .toList();
            String mediaType = params.get("mediaType");
            Integer releaseYear = parseIntSafe(params, "releaseYear");
            Integer ageRestriction = parseIntSafe(params, "ageRestriction");
            Integer rating = parseIntSafe(params, "rating");
            String sortBy = params.get("sortBy");
            List<MediaResponse> results = mediaService.searchMedia(
                    title, genres, mediaType, releaseYear, ageRestriction, rating, sortBy
            );
            HttpHelper.sendJSONResponse(exchange, 200, results);

        }


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
