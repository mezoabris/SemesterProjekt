package org.example;
import com.sun.net.httpserver.HttpServer;
import dataaccess.FavoriteDAO;
import dataaccess.MediaDAO;
import dataaccess.RatingDAO;
import dataaccess.UserDAO;
import handlers.*;
import config.DatabaseConfig;
import helpers.ConnectionProvider;
import helpers.DefaultConnectionProvider;
import helpers.GenreValidation;
import helpers.PasswordHasher;
import helpers.TokenHelper;
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
            PasswordHasher hasher = new PasswordHasher();
            UserDAO userDAO = new UserDAO();
            MediaDAO mediaDAO = new MediaDAO();
            RatingDAO ratingDAO = new RatingDAO();
            FavoriteDAO favoriteDAO = new FavoriteDAO(); // Created
            ConnectionProvider connectionProvider = new DefaultConnectionProvider();
            GenreValidation validator = new GenreValidation();
            AuthService authService = new AuthService(hasher, userDAO, connectionProvider);
            new TokenHelper(authService); // Initialize TokenHelper with authService

            MediaService mediaService = new MediaService(mediaDAO, connectionProvider);
            UserService userService = new UserService(validator, userDAO, ratingDAO, connectionProvider);
            FavoriteService favoriteService = new FavoriteService(favoriteDAO, mediaDAO, connectionProvider);
            RecommendationService recommendationService = new RecommendationService();
            RatingService ratingService = new RatingService(ratingDAO, connectionProvider);

            // Register handlers with their routes
            server.createContext("/api/users/register", new RegisterHandler(authService));
            server.createContext("/api/users/login", new LoginHandler(authService));
            server.createContext("/api/users", new UserHandler(userService, ratingService));
            server.createContext("/api/media", new MediaHandler(mediaService));
            server.createContext("/api/favorites", new FavoriteHandler(favoriteService));
            System.out.println("test");

            server.createContext("/api/ratings", new RatingHandler(ratingService));
            server.createContext("/api/recommendations", new RecommendationHandler(recommendationService));


            // Start server
            server.setExecutor(null);
            server.start();
        } catch (Exception e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}