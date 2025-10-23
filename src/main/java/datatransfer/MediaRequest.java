package datatransfer;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

public class MediaRequest {
    @Getter @Setter
    String creator;
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
    public MediaRequest(){}
    public MediaRequest(String creator,String title, String description, String mediaType, int releaseYear, List<String> genres, int ageRestriction) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.mediaType = mediaType;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.ageRestriction = ageRestriction;
    }

}
