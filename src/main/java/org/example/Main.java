package org.example;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import config.DatabaseConfig;
import service.*;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            // Test database connection
            Connection conn = DatabaseConfig.getConnection();

            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            AuthService authService = new AuthService();
            MediaService mediaService = new MediaService();
            UserService userService = new UserService();
            RecommendationService recommendationService = new RecommendationService();
            RatingService ratingService = new RatingService();

            // Register handlers with their routes
            server.createContext("/api/users/register", new RegisterHandler());
            server.createContext("/api/users/login", new LoginHandler());
            server.createContext("/api/users", new UserHandler(userService));
            server.createContext("/api/media", new MediaHandler());


            server.createContext("/api/ratings", new RatingHandler());
            server.createContext("/api/recommendations", new RecommendationHandler());

            // Start server
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}