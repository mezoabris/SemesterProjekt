package helpers;

import com.sun.net.httpserver.HttpExchange;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TokenHelper {


    public static String generateToken(String username) {
        return username + "-"+UUID.randomUUID().toString() + "-mrpToken";
    }
    public static String getUserName(String token) {
        if (token == null || !token.endsWith("-mrpToken")) {
            return null;
        }
        int firstDash = token.indexOf("-");
        if (firstDash > 0) {
            return token.substring(0, firstDash);
        }
        return null;
    }
    public static boolean isValidToken(String token) {
        if (token == null || !token.endsWith("-mrpToken")) {
            return false;
        }
        return token.endsWith("-mrpToken") && getUserName(token) != null;
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
