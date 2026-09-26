package service;

import dao.UserDAO;
import model.User;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Test
    void shouldRegisterUser() {

        UserDAO userDAO = mock(UserDAO.class);

        UserService userService = new UserService(userDAO);

        User user = new User(
                0,
                "Test User",
                "test@example.com",
                "hashedPassword",
                "BUYER"
        );

        userService.register(user);

        verify(userDAO).save(user);
    }

    @Test
    void shouldLoginUser() {

        UserDAO userDAO = mock(UserDAO.class);

        User user = new User(
                1,
                "Test User",
                "test@example.com",
                "hashedPassword",
                "BUYER"
        );

        when(userDAO.findByEmail("test@example.com"))
                .thenReturn(user);

        UserService userService = new UserService(userDAO);

        User result = userService.login("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void shouldReturnNullForUnknownUser() {

        UserDAO userDAO = mock(UserDAO.class);

        when(userDAO.findByEmail("unknown@example.com"))
                .thenReturn(null);

        UserService userService = new UserService(userDAO);

        User result = userService.login("unknown@example.com");

        assertNull(result);
    }
}
