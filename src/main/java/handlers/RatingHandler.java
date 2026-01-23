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
        String[] segments = path.split("/");
        String userAction = "";
        int ID = 0;
        // Parse: /api/ratings/{id} or /api/ratings/{id}/{action}
        if(segments.length >= 4) {
            try {
                ID = Integer.parseInt(segments[3]); // Always try to parse 4th segment as ID
                if(segments.length == 5) {
                    userAction = segments[4]; // like, approve, rate
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format: " + segments[3]);
                HttpHelper.sendJSONResponse(exchange, 400, "Invalid rating ID");
                return;
            }
        }

        User user = TokenHelper.requireValidToken(exchange);
        if(user == null) {
            HttpHelper.sendJSONResponse(exchange, 400, "Invalid token");
            return;
        }
        try{
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

       Rating ratingRequest = HttpHelper.parseRequestBody(exchange, Rating.class);
       ratingRequest.setMediaID(mediaID);
       RatingResponse response = ratingService.saveRating(user.getUserID(), ratingRequest);
       HttpHelper.sendJSONResponse(exchange, response.getStatus(), response.getMessage());
    }
}
