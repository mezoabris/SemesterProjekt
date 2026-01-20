package models;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    @Test
    void testRatingCreation() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Rating rating = new Rating(5, "Great!", now);

        assertEquals(5, rating.getStars());
        assertEquals("Great!", rating.getComment());
        assertEquals(now, rating.getCreatedAt());
    }

    @Test
    void testRatingSettersGetters() {
        Rating rating = new Rating();
        rating.setMediaID(1);
        rating.setUserID(10);
        rating.setStars(4);
        rating.setComment("Good");
        rating.setCommentApproved(true);

        assertEquals(1, rating.getMediaID());
        assertEquals(10, rating.getUserID());
        assertEquals(4, rating.getStars());
        assertEquals("Good", rating.getComment());
        assertTrue(rating.isCommentApproved());
    }

    @Test
    void testRatingDefaultApprovalFalse() {
        Rating rating = new Rating();
        rating.setCommentApproved(false);

        assertFalse(rating.isCommentApproved());
    }
}
