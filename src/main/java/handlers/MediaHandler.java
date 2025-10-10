package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.MediaRequest;
import datatransfer.MediaResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.MediaService;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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
                case "POST" -> handleWrite(exchange, user, segments); // ✅ unified handler
                case "PUT" -> handleWrite(exchange, user, segments);
                case "DELETE" -> handleDelete(exchange, segments);
                default -> HttpHelper.sendJSONResponse(exchange, 405, "Method not allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpHelper.sendJSONResponse(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }


    private void handleWrite(HttpExchange exchange, User user, String[] segments) throws IOException {
        try{
            MediaRequest request = HttpHelper.parseRequestBody(exchange, MediaRequest.class);
            request.setCreator(user.getUsername());
            MediaResponse response = mediaService.upsertMedia(request, segments);
            HttpHelper.sendJSONResponse(exchange, 200, response.getMessage());
        }catch (SQLException | IOException e ){
            HttpHelper.sendJSONResponse(exchange, 500, e.getMessage());
        }catch (IllegalArgumentException e){
            HttpHelper.sendJSONResponse(exchange, 400, e.getMessage());
        }
    }

    private void handleGet(HttpExchange exchange, String[] segments, Map<String, String> params) throws IOException, SQLException {
        MediaResponse response = mediaService.getMedia(exchange, segments, params);
        HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
    }

    private void handleDelete(HttpExchange exchange, String[] segments) throws IOException {
            MediaResponse response = mediaService.deleteMediaByID(exchange, segments);
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
    }

}
