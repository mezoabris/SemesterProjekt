package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        /*
          TODO login(loginRequest)-> AuthService-> UserDAO.create(User)
         */

    }
}
