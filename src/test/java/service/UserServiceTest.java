package service;

import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void shouldRegisterUser() {

        UserDAO fakeDAO = new UserDAO() {

            private User savedUser;

            @Override
            public void save(User user) {
                savedUser = user;
            }

            @Override
            public User findByEmail(String email) {
                return savedUser;
            }

            @Override
            public User findById(int id) {
                return null;
            }

            @Override
            public java.util.List<User> findAll() {
                return java.util.List.of();
            }
        };

        UserService userService = new UserService(fakeDAO);

        User user = new User(
                0,
                "Test User",
                "test@example.com",
                "hashedPassword",
                "BUYER"
        );

        userService.register(user);

        assertNotNull(userService.login("test@example.com"));
        assertEquals("test@example.com",
                userService.login("test@example.com").getEmail());
    }
}
