package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.RatingRequest;
import datatransfer.UserProfileResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.AuthService;
import service.RatingService;
import service.UserService;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements HttpHandler {
    private int userID;
    private final AuthService authService =  new AuthService();
    UserService userService;
    RatingService ratingService = new RatingService();
    public UserHandler(UserService userService) {

        this.userService = userService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();

        String method = exchange.getRequestMethod();

        String[] segments =path.split("/");

        String userAction = segments[segments.length - 1];

        this.userID = Integer.parseInt(segments[segments.length - 2]);

        if(TokenHelper.isValidToken(exchange, userID)){
            return;
        }




        switch (method) {
            case "GET":
                if(userAction.equals("profile")){
                    try {
                        System.out.println("Getting profile");
                        handleGetProfile(exchange);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(userAction.equals("ratings")){
                    try {
                        System.out.println("Getting ratings");
                        handleGetRatingHistory(exchange);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } {
                    HttpHelper.sendTextResponse(exchange, 404, "Not found");
                }
                break;
            case "POST":
                break;
            case "PUT":
                break;

            default:
                HttpHelper.sendTextResponse(exchange, 405, "Method not allowed");
        }




    }
    private void handleGetProfile(HttpExchange exchange) throws SQLException, IOException {


        User user = userService.findUserProfile(userID);
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

        /*
          TODO handleGetRatingHistory()

          TODO handleGetFavorites()
          TODO handleUpdateProfile()

         */
}
