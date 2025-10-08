package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.AuthRequest;
import service.AuthService;
import helpers.HttpHelper;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterHandler implements HttpHandler {
    private AuthService authService = new AuthService();

    @Override
    public void handle(HttpExchange httpExchange) throws RuntimeException, IOException {
        System.out.println("Handler reached!");
        String method = httpExchange.getRequestMethod();
        if(!method.equals("POST")) {
            throw new RuntimeException("Method not supported");
        }
        try {
            AuthRequest registerRequest = HttpHelper.parseRequestBody(httpExchange, AuthRequest.class);
            String token = authService.register(registerRequest);

            if (token == null) {
                HttpHelper.sendTextResponse(httpExchange, 400, "Registration failed");
            } else {
                HttpHelper.sendTextResponse(httpExchange, 201, token);
            }

        } catch (SQLException e) {
            HttpHelper.sendTextResponse(httpExchange, 500, "Database error: " + e.getMessage());
        } catch (Exception e) {
            HttpHelper.sendTextResponse(httpExchange, 500, "Server error: " + e.getMessage());
        }


    }
}
