package dao;

import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDAOImplTest {

    private static HikariDataSource dataSource;
    private static ServletContext context;
    private UserDAOImpl userDAO;

    @BeforeAll
    static void setUpDatabase() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(
                "jdbc:h2:mem:userdao_test;DB_CLOSE_DELAY=-1"
        );
        config.setUsername("sa");
        config.setPassword("");

        dataSource = new HikariDataSource(config);

        context = mock(ServletContext.class);

        when(context.getAttribute("dataSource"))
                .thenReturn(dataSource);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("""
                CREATE TABLE users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(150) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(20) NOT NULL
                )
                """)) {

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {

        userDAO = new UserDAOImpl(context);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("DELETE FROM users")) {

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldSaveAndFindByEmail() {

        User user = new User(
                0,
                "Test User",
                "test@example.com",
                "password123",
                "BUYER"
        );

        userDAO.save(user);

        User foundUser = userDAO.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("BUYER", foundUser.getRole());
    }

    @Test
    void shouldFindById() {

        User user = new User(
                0,
                "John",
                "john@example.com",
                "password123",
                "SELLER"
        );

        userDAO.save(user);

        User savedUser = userDAO.findByEmail("john@example.com");

        assertNotNull(savedUser);

        User foundUser = userDAO.findById(savedUser.getId());

        assertNotNull(foundUser);
        assertEquals("john@example.com", foundUser.getEmail());
        assertEquals("SELLER", foundUser.getRole());
    }

    @Test
    void shouldFindAllUsers() {

        User user1 = new User(
                0,
                "User One",
                "one@example.com",
                "password1",
                "BUYER"
        );

        User user2 = new User(
                0,
                "User Two",
                "two@example.com",
                "password2",
                "SELLER"
        );

        userDAO.save(user1);
        userDAO.save(user2);

        List<User> users = userDAO.findAll();

        assertEquals(2, users.size());
        assertEquals("one@example.com", users.get(0).getEmail());
        assertEquals("two@example.com", users.get(1).getEmail());
    }

    @AfterAll
    static void tearDownDatabase() {

        if (dataSource != null) {
            dataSource.close();
        }
    }
}
