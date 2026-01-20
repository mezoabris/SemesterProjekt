package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User("testuser", "hashedpass");

        assertEquals("testuser", user.getUsername());
        assertEquals("hashedpass", user.getPassword());
    }

    @Test
    void testUserSettersGetters() {
        User user = new User();
        user.setUserID(123);
        user.setUsername("john");
        user.setPassword("hash123");
        user.setToken("token123");
        user.setFavoriteGenre("sci-fi");

        assertEquals(123, user.getUserID());
        assertEquals("john", user.getUsername());
        assertEquals("hash123", user.getPassword());
        assertEquals("token123", user.getToken());
        assertEquals("sci-fi", user.getFavoriteGenre());
    }

    @Test
    void testDefaultFavoriteGenre() {
        User user = new User();
        user.setFavoriteGenre("Unknow");

        assertEquals("Unknow", user.getFavoriteGenre());
    }
}
