package datatransfer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RecommendationResponse {
    @Getter @Setter
    private int status;
    @Getter @Setter
    private String message;
    @Getter @Setter
    private List<MediaRequest> recommendations;

    public RecommendationResponse() {}

    public RecommendationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public RecommendationResponse(int status, String message, List<MediaRequest> recommendations) {
        this.status = status;
        this.message = message;
        this.recommendations = recommendations;
    }
}
