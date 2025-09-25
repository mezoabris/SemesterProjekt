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
            System.out.println("✅ Database connected successfully!");

            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            AuthService authService = new AuthService();
            MediaService mediaService = new MediaService();
            UserService userService = new UserService();
            RecommendationService recommendationService = new RecommendationService();
            RatingService ratingService = new RatingService();

            // Register handlers with their routes
            server.createContext("/api/users/register", new RegisterHandler());
            System.out.println("✅ register handler successfully!");
            server.createContext("/api/users/login", new LoginHandler());
            System.out.println("✅ login handler successfully!");
            server.createContext("/api/users", new UserHandler(userService));
            System.out.println("✅ user handler successfully!");
            server.createContext("/api/media", new MediaHandler());
            System.out.println("✅ media handler successfully!");
            server.createContext("/api/ratings", new RatingHandler());
            System.out.println("✅ ratings handler successfully!");
            server.createContext("/api/recommendations", new RecommendationHandler());
            System.out.println("✅ recommendation handler successfully!");

            // Start server
            server.setExecutor(null);
            server.start();

            System.out.println("✅ Server started on http://localhost:8080");
            System.out.println("📍 Available endpoints:");
            System.out.println("   POST /api/auth/register");
            System.out.println("   POST /api/auth/login");
            System.out.println("   GET  /api/users/{username}/profile");
            System.out.println("   POST /api/media");
            System.out.println("   GET  /api/media");
            System.out.println("   POST /api/ratings");
            System.out.println("   GET  /api/recommendations");

        } catch (Exception e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}