package datatransfer;

import lombok.Getter;
import lombok.Setter;

public class LeaderboardEntry {
    @Getter @Setter
    private String username;
    @Getter @Setter
    private int totalRatings;
    @Getter @Setter
    private double averageScore;
    public LeaderboardEntry( String username, int totalRatings, double averageScore){
        this.averageScore = averageScore;
        this.username = username;
        this.totalRatings = totalRatings;
    }
}
