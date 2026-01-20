package helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreValidationTest {

    @Test
    void validateGenre() {
        String wrongGenre = "not-genre";
        String rightGenre = "drama";
        GenreValidation genreValidation = new GenreValidation();
        assertFalse(genreValidation.validateGenre(wrongGenre));
        assertTrue(genreValidation.validateGenre(rightGenre));
    }
}