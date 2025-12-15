package dataaccess;

import datatransfer.RatingRequest;
import datatransfer.RatingResponse;
import models.Rating;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RatingDAOStub extends RatingDAO {
    private final List<Rating> ratings = new ArrayList<>();

    public RatingDAOStub() {
        Rating rating1 = new Rating();
        rating1.setMediaID(1);
        rating1.setStars(5);
        rating1.setComment("Great movie!");

        Rating rating2 = new Rating();
        rating2.setMediaID(2);
        rating2.setStars(4);
        rating2.setComment("Good show");

        ratings.add(rating1);
        ratings.add(rating2);
    }

    @Override
    public List<RatingRequest> getUserRatings(Connection con, int userID) throws SQLException {
        List<RatingRequest> ratingRequests = new ArrayList<>();
        for (Rating rating : ratings) {
            RatingRequest req = new RatingRequest(
                "Test Title",
                rating.getStars(),
                rating.getComment(),
                new Timestamp(System.currentTimeMillis())
            );
            ratingRequests.add(req);
        }
        return ratingRequests;
    }

    @Override
    public RatingResponse createRating(Connection con, int userID, Rating ratingRequest) {
        ratings.add(ratingRequest);
        RatingResponse response = new RatingResponse();
        response.setStatus(200);
        response.setMessage("Successfully created rating");
        return response;
    }

    @Override
    public RatingResponse updateRating(Connection con, int mediaID, Rating ratingRequest, int userID) {
        RatingResponse response = new RatingResponse();
        for (Rating rating : ratings) {
            if (rating.getMediaID() == mediaID) {
                rating.setStars(ratingRequest.getStars());
                rating.setComment(ratingRequest.getComment());
                response.setStatus(200);
                response.setMessage("Successfully updated rating");
                return response;
            }
        }
        response.setStatus(404);
        response.setMessage("Rating not found");
        return response;
    }

    @Override
    public Rating findByUserAndMedia(Connection con, int userID, int mediaID) {
        for (Rating rating : ratings) {
            if (rating.getMediaID() == mediaID) {
                return rating;
            }
        }
        return null;
    }
}
