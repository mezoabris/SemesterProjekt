package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.RecommendationResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.RecommendationService;

import java.io.IOException;

public class RecommendationHandler implements HttpHandler {
    private final RecommendationService recommendationService;
    public RecommendationHandler(RecommendationService recommendationService){
        this.recommendationService = recommendationService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        User user = TokenHelper.requireValidToken(exchange);
        if (user == null) return;

        if ("GET".equals(method)) {
            handleGetRecommendations(exchange, user);
        } else {
            HttpHelper.sendResponse(exchange, 405, "Method not allowed");
        }
    }

    private void handleGetRecommendations(HttpExchange exchange, User user) throws IOException {
        RecommendationResponse response = recommendationService.getRecommendations(user.getId());
        HttpHelper.sendJsonResponse(exchange, response.getStatus(), response);
    }
}
