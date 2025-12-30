package service;

import dataaccess.LeaderboardDAO;
import datatransfer.LeaderboardResponse;
import helpers.ConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class LeaderboardService {
    private final ConnectionProvider connectionProvider;
    private final LeaderboardDAO leaderboardDAO;
    public LeaderboardService(ConnectionProvider connectionProvider, LeaderboardDAO leaderboardDAO){
        this.leaderboardDAO= leaderboardDAO;
        this.connectionProvider = connectionProvider;
    }
    public LeaderboardResponse getUserLeaderBoard(){
        Connection con = null;
        LeaderboardResponse res;
        try{
            con = connectionProvider.getConnection();
            res = leaderboardDAO.getLeaderboard(con);
            res.setStatus(200);
            res.setMessage("Leaderboard returned successfully.");



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return res;

    }


}
