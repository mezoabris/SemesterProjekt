package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.Rating;
import models.User;
import service.RatingLikeService;
import service.RatingService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class RatingHandler implements HttpHandler {

    private final RatingService ratingService;
    private final RatingLikeService ratingLikeService;
    public RatingHandler(RatingService ratingService, RatingLikeService ratingLikeService){
        this.ratingService = ratingService;
        this.ratingLikeService = ratingLikeService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    /*
    TODO rateMedia(RatingRequest)

     */
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] segments =path.split("/");
        System.out.println(Arrays.toString(segments));
        String userAction = "";
        int ID = 0;
        if(segments.length > 2){
            userAction = segments[segments.length - 1];
            ID = Integer.parseInt(segments[segments.length - 2]);
        }

        User user = TokenHelper.requireValidToken(exchange);
        if(user == null) HttpHelper.sendJSONResponse(exchange, 400, "Invalid token");
        try{
            System.out.println(method);
            switch (method) {
                case "GET" -> handleGet(exchange, user);
                case "POST", "PUT" -> handleWrite(exchange, user, userAction, ID);
                case "DELETE" -> handleDel(exchange, user,userAction ,ID);
            }
        }catch(Exception e){
            HttpHelper.sendJSONResponse(exchange, 500, "Internal Server Error");
        }



    }

    private void handleDel(HttpExchange exchange, User user, String userAction, int ID) throws IOException, SQLException{
        RatingResponse response = new RatingResponse();
        try{
            System.out.println(userAction);
            System.out.println("ID: " + ID);
            System.out.println("user.getUserID(): "+ user.getUserID());
            switch (userAction){
                case "like" -> response = ratingLikeService.deleteLike(ID, user.getUsername());
                case "rate" -> response = ratingService.removeRating(ID, user.getUserID());
            }
            HttpHelper.sendJSONResponse(exchange, response.getStatus(), response.getMessage());
        }catch(Exception e){
            HttpHelper.sendTextResponse(exchange, 500, e.getMessage());
        }

    }

    private void handleGet(HttpExchange exchange, User user) throws IOException, SQLException {
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        if(segments.length > 2) {
            int mediaID = Integer.parseInt(segments[segments.length - 1]);
            HttpHelper.sendJSONResponse(exchange, 200, ratingService.findAllRatings(mediaID));
        } else {
            HttpHelper.sendJSONResponse(exchange, 400, "Media ID is required");
        }
    }

    private void handleWrite(HttpExchange exchange, User user, String userAction, int ID) throws IOException {

        System.out.println("Handling write");
        try{
            switch (userAction) {
                case "rate"-> handleRate(exchange, ID, user);
                case "like"-> handleLike(exchange, ID, user);
                case "approve" -> handleApprove(exchange, ID, user);
            }
        }catch(Exception e){
            HttpHelper.sendTextResponse(exchange, 500, e.getMessage());
        }

    }

    private void handleApprove(HttpExchange exchange, int ratingID, User user) throws IOException, SQLException {
        RatingResponse response = ratingService.approveRating(ratingID, user.getUsername());
        HttpHelper.sendJSONResponse(exchange, response.getStatus(), response.getMessage());
    }



    private void handleLike(HttpExchange exchange, int id, User user) throws SQLException, IOException {
        RatingResponse response = ratingLikeService.likeRating(id, user.getUsername());
        HttpHelper.sendJSONResponse(exchange, response.getStatus(), response.getMessage());

    }

    private void handleRate(HttpExchange exchange, int mediaID, User user) throws IOException, SQLException {
        System.out.println("Handling rating request");

       Rating ratingRequest = HttpHelper.parseRequestBody(exchange, Rating.class);
       ratingRequest.setMediaID(mediaID);
       System.out.println("ratingRequest: " + ratingRequest);
       RatingResponse response = ratingService.saveRating(user.getUserID(), ratingRequest);
       HttpHelper.sendJSONResponse(exchange, response.getStatus(), response.getMessage());
    }
}
