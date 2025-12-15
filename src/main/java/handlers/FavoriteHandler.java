package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.MediaResponse;
import datatransfer.MediaRequest;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.FavoriteService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class FavoriteHandler implements HttpHandler {
    private final FavoriteService favoriteService;

    public FavoriteHandler(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        System.out.println(Arrays.toString(segments));

        System.out.println(method);


        try {
            User user = TokenHelper.requireValidToken(exchange);
            if (user == null) return; // Response handled in TokenHelper

            if (segments.length == 3 && method.equals("GET")) {
                handleGetFavorites(exchange, user);
            } else if (segments.length == 4 && method.equals("POST")) {
                int mediaId = Integer.parseInt(segments[3]);
                handleAddFavorite(exchange, mediaId, user);
            } else if (segments.length == 4 && method.equals("DELETE")) {
                int mediaId = Integer.parseInt(segments[3]);
                handleRemoveFavorite(exchange, mediaId, user);
            } else {
                HttpHelper.sendJSONResponse(exchange, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpHelper.sendJSONResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }

    private void handleGetFavorites(HttpExchange exchange, User user) throws IOException, SQLException {
        List<MediaRequest> favorites = favoriteService.getFavoritesByUserId(user.getUserID());

        HttpHelper.sendJSONResponse(exchange, 200, favorites);
    }

    private void handleAddFavorite(HttpExchange exchange, int mediaId , User user) throws IOException, SQLException {
        try {

            MediaResponse response = favoriteService.addFavorite(mediaId, user.getUserID());
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
        } catch (NumberFormatException e) {
            HttpHelper.sendJSONResponse(exchange, 400, "Invalid Media ID");
        }
    }

    private void handleRemoveFavorite(HttpExchange exchange, int mediaId, User user) throws IOException, SQLException {
        try {
            MediaResponse response = favoriteService.removeFavorite(mediaId, user.getUserID());
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response);
        } catch (NumberFormatException e) {
            HttpHelper.sendJSONResponse(exchange, 400, "Invalid Media ID");
        }
    }
}
