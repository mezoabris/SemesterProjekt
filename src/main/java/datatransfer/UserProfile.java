package datatransfer;
import lombok.Getter;
import lombok.Setter;

public class UserProfile {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String favoritegenre;

    public UserProfile() {}

    public UserProfile(String username, String favoritegenre) {
        this.username = username;
        this.favoritegenre = favoritegenre;
    }

}
