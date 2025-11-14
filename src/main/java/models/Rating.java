package models;

import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

public class Rating {
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
    private Timestamp createdAt;
    @Getter
    @Setter
    private Timestamp updatedAt;

}
