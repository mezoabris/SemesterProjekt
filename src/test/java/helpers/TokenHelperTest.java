package helpers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenHelperTest {

    @Test
    void testGenerateToken() {
        String username = "testuser";
        String token = TokenHelper.generateToken(username);

        assertNotNull(token);
        assertTrue(token.contains(username));
        assertTrue(token.contains("-mrpToken"));
    }

    @Test
    void testGenerateTokenDifferentUsers() {
        String token1 = TokenHelper.generateToken("user1");
        String token2 = TokenHelper.generateToken("user2");

        assertNotEquals(token1, token2);
    }

    @Test
    void testGenerateTokenNotEmpty() {
        String token = TokenHelper.generateToken("user");
        assertFalse(token.isEmpty());
    }
}
