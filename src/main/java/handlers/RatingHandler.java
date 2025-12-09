package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.Rating;
import models.User;
import service.RatingService;

import java.io.IOException;
import java.sql.SQLException;

public class RatingHandler implements HttpHandler {

    RatingService  ratingService = new RatingService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    /*
    TODO rateMedia(RatingRequest)

     */
        System.out.println("Getting ratings");
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        System.out.println("Method: " + method);
        String[] segments =path.split("/");
        String userAction = segments[segments.length - 1];
        int ID = Integer.parseInt(segments[segments.length - 2]);
        User user = TokenHelper.requireValidToken(exchange);
        if(user == null) HttpHelper.sendJSONResponse(exchange, 400, "Invalid token");
        System.out.println("validation ok");
        try{
            switch (method) {
                case "POST", "PUT" -> handleWrite(exchange, user, userAction, ID);
            }
        }catch(Exception e){
            HttpHelper.sendJSONResponse(exchange, 500, "Internal Server Error");
        }



    }

    private void handleWrite(HttpExchange exchange, User user, String userAction, int ID) throws IOException {
        /*
        if the action is rating, then the value at segments[segments.length-2]
        is referring to the mediaID, in other cases like liking a rating it refers to the rating_id
         */
        System.out.println("Handling write");
        try{
            switch (userAction) {
                case "rate"-> handleRate(exchange, ID, user);
                case "like"-> handleLike(exchange, ID, user);
                case "confirm"->handleConfirm(exchange, ID, user);
            }
        }catch(Exception e){

        }

    }

    private void handleConfirm(HttpExchange exchange, int id, User user) {
    }

    private void handleLike(HttpExchange exchange, int id, User user) {
    }

    private void handleRate(HttpExchange exchange, int mediaID, User user) throws IOException {
        System.out.println("Handling rating request");

       Rating ratingRequest = HttpHelper.parseRequestBody(exchange, Rating.class);
       ratingRequest.setMediaID(mediaID);
       System.out.println("ratingRequest: " + ratingRequest);
       RatingResponse response = ratingService.saveRating(user.getUserID(), ratingRequest);
       HttpHelper.sendJSONResponse(exchange, response.getStatus(), response.getMessage());
    }
}
