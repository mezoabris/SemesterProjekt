package datatransfer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MediaResponse {
    @Getter @Setter
    int status;
    @Getter @Setter
    String message;
    @Getter @Setter
    private List<MediaRequest> requests;

    public MediaResponse() {

    }

    public MediaResponse(int status, String message){
        this.status = status;
        this.message = message;
    }
}
