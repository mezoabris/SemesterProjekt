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
    private int status;
    @Getter
    @Setter
    private User user;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private List<Rating> ratings;
    @Getter
    @Setter
    private List<MediaRequest> favorites;


}
