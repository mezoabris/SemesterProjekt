package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.UserDAO;
import datatransfer.*;
import helpers.HttpHelper;
import helpers.PasswordHasher;
import helpers.TokenHelper;
import models.MediaEntry;
import models.User;
import service.AuthService;
import service.MediaService;
import service.RatingService;
import service.UserService;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserHandler implements HttpHandler {
    private int userID;



    private final UserService userService;
    private final RatingService ratingService;
    public UserHandler(UserService userService, RatingService ratingService) {
        this.ratingService = ratingService;
        this.userService = userService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        String method = exchange.getRequestMethod();
        System.out.println("Method: " + method);
        String[] segments =path.split("/");

        String userAction = segments[segments.length - 1];

        Map<String, String> params = HttpHelper.getQueryParams(exchange);

        this.userID = Integer.parseInt(segments[segments.length - 2]);

        User user = TokenHelper.requireValidToken(exchange);
        if (user == null) return;

        if(user.getUserID() != this.userID){
            HttpHelper.sendJSONResponse(exchange, 403, "Forbidden");
            return;
        }
        try {


            switch (method) {
                case "GET" -> handleGet(exchange, userAction);
                case "POST", "PUT" -> handleWrite(exchange, user, segments);
                default -> HttpHelper.sendJSONResponse(exchange, 403, "Method not allowed");
            }
        }catch (Exception e){
            HttpHelper.sendJSONResponse(exchange, 500, "Internal Server Error");
        }








    }
    private void handleGet(HttpExchange exchange, String action) throws IOException {
        try{
            switch (action) {
                case "profile" -> handleGetProfile(exchange);
                case "ratings" -> handleGetRatingHistory(exchange);
                case "favorites"-> handleGetFavorites(exchange);
            }
        }catch (SQLException e){}
    }

    private void handleGetFavorites(HttpExchange exchange) {
        System.out.println("Getting favorites");
        FavoritesRequest favorites = new FavoritesRequest();
        try{

            favorites = userService.findUserFavorites(this.userID);

            HttpHelper.sendJSONResponse(exchange, 200, favorites );


        }catch (SQLException e){
            System.out.println(e);

        }catch (Exception e){
            System.out.println(e);
        }


    }

    private void handleWrite(HttpExchange exchange, User user, String[] segments) throws IOException {
        System.out.println("Writing profile");
        UserProfile profileChanges = HttpHelper.parseRequestBody(exchange, UserProfile.class);
        System.out.println("Profile changes: " + profileChanges);
        try{
            UserResponse userResponse = userService.editProfile(this.userID,profileChanges);
            HttpHelper.sendJSONResponse(exchange,  userResponse.getStatus(), userResponse.getMessage());

        }catch (Exception e){
            System.out.println("Exception: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleGetProfile(HttpExchange exchange) throws SQLException, IOException {


        User user = userService.findUserProfile(this.userID);
        System.out.println("User found: " + (user != null));

        if (user == null) {
            System.out.println("Sending 404");
            HttpHelper.sendTextResponse(exchange, 404, "User not found");
            return;
        }

        System.out.println("Sending user JSON");
        HttpHelper.sendJSONResponse(exchange, 200, user);
    }
    public void handleGetRatingHistory(HttpExchange exchange) throws SQLException, IOException {
        List<RatingRequest> ratings = ratingService.findAllRatings(userID);
        if (ratings.isEmpty()) {
            HttpHelper.sendTextResponse(exchange, 200, "No ratings found");
        }
        HttpHelper.sendJSONResponse(exchange, 200, ratings);
    }


}
