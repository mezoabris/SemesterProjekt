package datatransfer;

import lombok.Getter;
import lombok.Setter;

public class RatingRequest {
    @Getter
    @Setter
    int status;
    @Getter
    @Setter
    String message;
    @Getter
    @Setter
    java.sql.Timestamp createdAt;
    @Getter
    @Setter
    String title;
    @Getter
    @Setter
    int stars;
    @Getter
    @Setter
    String comment;
    @Getter
    @Setter
    boolean commentApproved;

    public RatingRequest(String title, int stars, String comment, java.sql.Timestamp createdAt ) {
        this.title = title;
        this.stars = stars;
        this.comment = comment;
        this.createdAt = createdAt;
    }


}
