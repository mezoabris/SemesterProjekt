package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.UserProfileResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.AuthService;
import service.UserService;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements HttpHandler {
    AuthService authService = new AuthService();
    UserService userService;
    public UserHandler(UserService userService) {

        this.userService = userService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Handler reached for path: " + exchange.getRequestURI());
        String path = exchange.getRequestURI().getPath();
        System.out.println("path: " + path);
        String method = exchange.getRequestMethod();
        System.out.println("method: " + method);
        String[] segments =path.split("/");

        String userAction = segments[segments.length - 1];
        System.out.println("userAction: " + userAction);
        int userID = Integer.parseInt(segments[segments.length - 2]);

        System.out.println("userID: " + userID);
        String tokenSent = TokenHelper.extractToken(exchange);
        System.out.println("tokenSent: " + tokenSent);
        try {
            boolean tokenValid = authService.isTokenValid(userID, tokenSent);
            System.out.println("tokenValid: " + tokenValid);
            if(!tokenValid){
                HttpHelper.sendTextResponse(exchange, 400, "the token is invalid, access isnt permitted");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        switch (method) {
            case "GET":
                if(userAction.equals("profile")){
                    try {
                        System.out.println("Getting profile");
                        handleGetProfile(userID, exchange);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    HttpHelper.sendTextResponse(exchange, 404, "Not found");
                }
                break;
            default:
                HttpHelper.sendTextResponse(exchange, 405, "Method not allowed");
        }




    }
    private void handleGetProfile(int userID, HttpExchange exchange) throws SQLException, IOException {
        System.out.println("handleGetProfile called for userID: " + userID);

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
        /*TODO handleGetProfile()
          TODO handleGetRatingHistory()
          TODO handleGetFavorites()
          TODO handleUpdateProfile()

         */
}
