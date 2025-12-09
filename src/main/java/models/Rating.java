package models;

import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

public class Rating {
    @Getter
    @Setter
    int mediaID;
    @Getter
    @Setter
    private int stars;
    @Getter
    @Setter
    private String comment;
    @Getter
    @Setter
    private boolean commentApproved;
    @Getter
    @Setter
    private java.sql.Timestamp  createdAt;
    @Getter
    @Setter
    private java.sql.Timestamp updatedAt;
    public Rating(int stars, String comment, java.sql.Timestamp createdAt ) {
        this.stars = stars;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    public Rating(){};

}
