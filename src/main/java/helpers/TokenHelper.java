package helpers;

import com.sun.net.httpserver.HttpExchange;
import dataaccess.UserDAO;
import models.User;
import service.AuthService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TokenHelper {
    private static AuthService authService;
    public TokenHelper(AuthService authService){
        TokenHelper.authService = authService;
    }



    public static String generateToken(String username) {
        return username + "-"+UUID.randomUUID().toString() + "-mrpToken";
    }
    public static String getUserName(String token) {
        if (token == null || !token.endsWith("-mrpToken")) {
            return null;
        }
        // Token format: username + "-" + UUID + "-mrpToken"
        // UUID length = 36
        // "-mrpToken" length = 9
        // Separator "-" length = 1
        // Total suffix length = 46
        int suffixLength = 36 + 1 + 9;
        
        if (token.length() <= suffixLength) {
            return null;
        }
        
        return token.substring(0, token.length() - suffixLength);
    }
    public static boolean isValidToken(HttpExchange exchange, int userID) throws IOException {
        String tokenSent = TokenHelper.extractToken(exchange);
        try{
            boolean tokenValid = authService.isTokenValid(userID, tokenSent);
            if (!tokenValid) {
                HttpHelper.sendJSONResponse(exchange, 401, "Invalid token");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;

    }
    public static User getUserFromToken(String token) {
        if (token == null || !token.endsWith("-mrpToken")) {
            return null;
        }

        try {
            return authService.findUserByToken(token); // DB lookup
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static User requireValidToken(HttpExchange exchange) throws IOException {
        String token = extractToken(exchange);
        User user = getUserFromToken(token);
        if (user == null) {
            HttpHelper.sendJSONResponse(exchange, 401, "Invalid token");
        }
        return user;
    }
    public static String extractToken(HttpExchange exchange) {

        if(exchange == null){
            return null;
        }
        Map<String, List<String>> headers = exchange.getRequestHeaders();

        List<String> authHeaders = headers.getOrDefault("Authorization", headers.getOrDefault("authorization", null));

        if (authHeaders == null || authHeaders.isEmpty()) { return null;}

        String header = authHeaders.get(0);

        String BEARER = "Bearer ";

        if(header.length() <= BEARER.length()){ return null;}

        if(header.regionMatches(true, 0, BEARER, 0, BEARER.length())){
            String token = header.substring(BEARER.length()).trim();
            return token.isEmpty() ? null : token;
        }
        return null;
    }
}
