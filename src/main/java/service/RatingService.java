package service;

import dataaccess.RatingDAO;
import dataaccess.UserDAO;
import datatransfer.RatingRequest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingService {
    RatingDAO ratingDAO = new RatingDAO();
    public List<RatingRequest> findAllRatings(int userID) throws SQLException {
        return ratingDAO.getUserRatings(userID);
    }
}
