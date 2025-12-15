package service;

import dataaccess.RatingDAOStub;
import dataaccess.UserDAOStub;
import datatransfer.UserProfile;
import datatransfer.UserResponse;
import helpers.ConnectionProvider;
import helpers.GenreValidation;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setup() throws SQLException {
        GenreValidation validator = new GenreValidation();
        UserDAOStub userStub = new UserDAOStub();
        RatingDAOStub ratingStub = new RatingDAOStub();
        ConnectionProvider provider = mock(ConnectionProvider.class);
        when(provider.getConnection()).thenReturn(mock(Connection.class));

        userService = new UserService(validator, userStub, ratingStub, provider);
    }
    @Test
    void findAll() throws SQLException {
        List<User> allUsers = userService.findAll();
        assertEquals(3, allUsers.size());
    }

    @Test
    void testFindUserProfile() throws Exception {
        UserResponse res = new UserResponse();
        res = userService.findUserProfile(1);
        assertEquals("test1", res.getUser().getUsername());
    }

    @Test
    void editProfile_invalidGenre_returnsError() throws SQLException {
        UserProfile editedUser = new UserProfile("test1", "pass1");
        editedUser.setFavoritegenre("invalidGenre");

        UserResponse res = userService.editProfile(1, editedUser);

        assertEquals(400, res.getStatus());
        assertEquals("Please enter a valid genre", res.getMessage());
    }

    @Test
    void editProfile_valid_Genre() throws SQLException{
        UserProfile editedUser = new UserProfile("test1", "pass1");
        editedUser.setFavoritegenre("comedy");

        UserResponse res = userService.editProfile(1, editedUser);

        assertEquals(200, res.getStatus());
        assertEquals("Profile updated successfully", res.getMessage());
    }
}