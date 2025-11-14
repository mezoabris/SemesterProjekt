package datatransfer;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class FavoritesRequest {
    @Getter
    @Setter
    List<MediaRequest> favorites;
    public FavoritesRequest() {};


}
