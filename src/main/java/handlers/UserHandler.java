package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import models.User;
import service.UserService;


import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserHandler implements HttpHandler {
    UserService userService;
    public UserHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        /*TODO getProfile()
          TODO getRatingHistory()
          updateProfile()

         */



    }
}
