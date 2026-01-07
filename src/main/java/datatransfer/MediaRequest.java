package datatransfer;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

public class MediaRequest {
    @Getter @Setter
    Integer mediaId;
    @Getter @Setter
    Integer creatorId;
    @Getter @Setter
    String creatorUsername; // For display purposes
    @Getter @Setter
    String title;
    @Getter @Setter
    String description;
    @Getter @Setter
    String mediaType;
    @Getter @Setter
    int releaseYear;
    @Getter @Setter
    List<String> genres;
    @Getter @Setter
    int ageRestriction;
    @Getter @Setter
    Double averageRating;
    public MediaRequest(){}
    public MediaRequest(Integer creatorId, String title, String description, String mediaType, int releaseYear, List<String> genres, int ageRestriction) {
        this.creatorId = creatorId;
        this.title = title;
        this.description = description;
        this.mediaType = mediaType;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.ageRestriction = ageRestriction;
    }

}
