package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


public class SimpleServer {
    public static void main(String[] args) {
        try{
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", new StartHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Server started at: http://localhost:8080");
        }catch (IOException e){
            System.out.println("Error starting the server");
        }
    }
}


