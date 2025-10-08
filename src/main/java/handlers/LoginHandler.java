package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.AuthRequest;
import helpers.HttpHelper;
import models.User;
import service.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginHandler implements HttpHandler {
    private AuthService authService = new AuthService();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        /*
          TODO login(loginRequest)-> AuthService-> UserDAO.create(User)
         */
        String method = httpExchange.getRequestMethod();
        if(!method.equals("POST")) httpExchange.sendResponseHeaders(400,0);
        try{
            AuthRequest loginRequest = HttpHelper.parseRequestBody(httpExchange,  AuthRequest.class);
            User user = authService.login(loginRequest);
            if(user == null){
                HttpHelper.sendTextResponse(httpExchange, 400, "Invalid username or password");
                return;
            }else{
                HttpHelper.sendTextResponse(httpExchange, 200, user.getToken());
            }
        } catch (SQLException e) {
            HttpHelper.sendTextResponse(httpExchange, 500, "Database error: " + e.getMessage());
        } catch (Exception e) {
            HttpHelper.sendTextResponse(httpExchange, 500, "Server error: " + e.getMessage());
        }

    }
}
