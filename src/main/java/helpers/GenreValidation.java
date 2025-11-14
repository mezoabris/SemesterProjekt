package helpers;

import java.util.Set;

public class GenreValidation {
    private static final Set<String> VALID_GENRES = Set.of(
            "action", "comedy", "drama", "horror", "sci-fi",
            "thriller", "romance", "fantasy", "documentary", "animation"
    );
    public boolean validateGenre(String genre){
        return VALID_GENRES.contains(genre);
    }
}
