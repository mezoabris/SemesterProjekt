package datatransfer;
import lombok.Setter;
import lombok.Getter;
import models.MediaEntry;
import models.Rating;
import models.User;

import java.util.List;

public class UserResponse {
    @Getter
    @Setter
    private int totalRatings;
    @Getter
    @Setter
    private double averageScore;
    @Getter
    @Setter
    private int status;
    @Getter
    @Setter
    private User user;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private List<RatingRequest> ratings;
    @Getter
    @Setter
    private List<MediaRequest> favorites;


}
