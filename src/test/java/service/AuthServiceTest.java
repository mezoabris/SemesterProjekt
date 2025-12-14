package service;

import dataaccess.UserDAO;
import datatransfer.AuthRequest;
import helpers.ConnectionProvider;
import helpers.PasswordHasher;
import models.User;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private UserDAO userDAO;
    private PasswordHasher hasher;
    private ConnectionProvider connectionProvider;
    private Connection conn;
    private AuthService authService;

    @BeforeEach
    void setup() throws SQLException {
        userDAO = mock(UserDAO.class);
        hasher = mock(PasswordHasher.class);
        connectionProvider = mock(ConnectionProvider.class);
        conn = mock(Connection.class);

        when(connectionProvider.getConnection()).thenReturn(conn);

        authService = new AuthService(hasher, userDAO, connectionProvider);
    }
    @Test
    void testRegister() throws SQLException {
        AuthRequest req = new AuthRequest("name", "pass");
        when(userDAO.findByUsername(eq(conn), eq(req.getUsername()))).thenReturn(null);
        when(hasher.hashPassword("pass")).thenReturn("hashedPassword");

        User createdUser = new User(req.getUsername(), "hashedPassword");
        when(userDAO.create(eq(conn), any(User.class))).thenReturn(createdUser);

        String token = authService.register(req);

        assertNotNull(token);
        verify(userDAO).updateToken(eq(conn), eq("name"),anyString());

    }
    @Test
    void registerExistingUser() throws SQLException {
        AuthRequest req = new AuthRequest("name", "password");
        when(userDAO.findByUsername(eq(conn),eq( "name"))).thenReturn(new User());

        assertThrows(SQLException.class, () -> authService.register(req));
    }

    @Test
    void login() throws SQLException {
        AuthRequest req = new AuthRequest("name", "pass");
        User existingUser = new User("name", "pass");
        when(userDAO.findByUsername(eq(conn), eq("name"))).thenReturn(existingUser);
        User returned = authService.login(req);
        assertEquals(returned.getUsername(), existingUser.getUsername());
        verify(userDAO).findByUsername(eq(conn),eq( "name"));
        verify(userDAO).updateToken(eq(conn), eq("name"), anyString());
    }
}