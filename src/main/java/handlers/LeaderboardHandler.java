package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import datatransfer.LeaderboardResponse;
import helpers.HttpHelper;
import helpers.TokenHelper;
import models.User;
import service.LeaderboardService;

import java.io.IOException;

public class LeaderboardHandler implements HttpHandler {
    private final LeaderboardService leaderboardService;
    public LeaderboardHandler(LeaderboardService leaderboardService){
        this.leaderboardService = leaderboardService;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        User user = TokenHelper.requireValidToken(exchange);
        if (user == null) return;

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");
        LeaderboardResponse res = new LeaderboardResponse();
        if(!method.equals("GET")){
            res.setStatus(400);
            res.setMessage("Invalid method");

        }else{

            res = leaderboardService.getUserLeaderBoard();

        }
        HttpHelper.sendJSONResponse(exchange, res.getStatus(), res);



    }
}
