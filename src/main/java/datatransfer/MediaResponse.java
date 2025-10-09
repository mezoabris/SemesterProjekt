package datatransfer;

import lombok.Getter;
import lombok.Setter;

public class MediaResponse {
    @Getter @Setter
    int status;
    @Getter @Setter
    String message;

    public MediaResponse() {

    }

    public MediaResponse(int status, String message){
        this.status = status;
        this.message = message;
    }
}
