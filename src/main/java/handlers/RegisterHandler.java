package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dataaccess.UserDAO;
import datatransfer.AuthRequest;
import datatransfer.AuthResponse;
import helpers.PasswordHasher;
import models.User;
import service.AuthService;
import helpers.HttpHelper;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {
    private final AuthService authService;
    public RegisterHandler(AuthService authService){
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws RuntimeException, IOException {
        System.out.println("Handler reached!");
        String method = httpExchange.getRequestMethod();
        if(!method.equals("POST")) {
            throw new RuntimeException("Method not supported");
        }
        try {
            AuthRequest registerRequest = HttpHelper.parseRequestBody(httpExchange, AuthRequest.class);
            User user = authService.registerAndReturnUser(registerRequest);

            if (user == null) {
                HttpHelper.sendTextResponse(httpExchange, 400, "Registration failed");
            } else {
                AuthResponse response = new AuthResponse(user.getToken(), user.getUserID(), user.getUsername());
                HttpHelper.sendJSONResponse(httpExchange, 201, response);
            }

        } catch (SQLException e) {
            HttpHelper.sendTextResponse(httpExchange, 500, "Database error: " + e.getMessage());
        } catch (Exception e) {
            HttpHelper.sendTextResponse(httpExchange, 500, "Server error: " + e.getMessage());
        }


    }
}
