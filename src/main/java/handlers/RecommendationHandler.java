package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.RecommendationService;

import java.io.IOException;

public class RecommendationHandler implements HttpHandler {
    private final RecommendationService recommendationService;
    public RecommendationHandler(RecommendationService recommendationService){
        this.recommendationService = recommendationService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {


    }
}
