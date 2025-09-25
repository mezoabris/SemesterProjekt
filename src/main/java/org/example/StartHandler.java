package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class StartHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        String print = "welcome to the MRP Media Rating Platform!";
        t.sendResponseHeaders(200, print.length());
        OutputStream os = t.getResponseBody();
        os.write(print.getBytes());
        os.close();
    }
}
