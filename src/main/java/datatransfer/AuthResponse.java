package datatransfer;

import lombok.Getter;
import lombok.Setter;

public class AuthResponse {
    @Getter @Setter
    private String token;

    @Getter @Setter
    private Integer userId;

    @Getter @Setter
    private String username;

    public AuthResponse() {}

    public AuthResponse(String token, Integer userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }
}
