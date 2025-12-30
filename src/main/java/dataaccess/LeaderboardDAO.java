package dataaccess;

import datatransfer.LeaderboardEntry;
import datatransfer.LeaderboardResponse;

import java.sql.*;

public class LeaderboardDAO {
    public LeaderboardResponse getLeaderboard(Connection conn) throws SQLException {
        LeaderboardResponse res = new LeaderboardResponse();
        String sql = "SELECT u.username, COUNT(*) AS rates, ROUND(AVG(r.stars), 2) AS average FROM users u " +
                     "JOIN ratings r ON u.id = r.user_id " +
                     "GROUP BY u.id, u.username ORDER BY rates DESC";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                int totalRatings = rs.getInt("rates");
                double averageScore = rs.getDouble("average");
                String username = rs.getString("username");
                LeaderboardEntry entry = new LeaderboardEntry(username, totalRatings, averageScore);
                res.addLeaderboardEntry(entry);
            }



            }catch (SQLException e) {
            throw e;
        }
        return res;
    }
}
