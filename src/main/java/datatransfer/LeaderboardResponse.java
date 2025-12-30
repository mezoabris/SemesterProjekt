package datatransfer;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

public class LeaderboardResponse {
    @Getter
    @Setter
    List<LeaderboardEntry> leaderboard = new ArrayList<>();
    @Getter
    @Setter
    private int status;
    @Getter
    @Setter
    private String message;
    public void addLeaderboardEntry( LeaderboardEntry entry){
        this.leaderboard.add(entry);
    }
}
