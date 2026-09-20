package listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Collectors;

@WebListener
public class DatabaseListener implements ServletContextListener {

    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent event) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:h2:mem:shakthimart;DB_CLOSE_DELAY=-1");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");

        dataSource = new HikariDataSource(config);

        // Create users table
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    role VARCHAR(20) NOT NULL
                )
                """)) {

            statement.execute();

            System.out.println("Users table created successfully!");

            // Run seed.sql
            runSeedData(connection, event.getServletContext());

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Unable to create users table",
                    e
            );
        }

        event.getServletContext().setAttribute("dataSource", dataSource);

        System.out.println(
                "Shakthi Mart database connected successfully!"
        );
    }

    private void runSeedData(
            Connection connection,
            ServletContext context) {

        try (InputStream inputStream =
                     context.getResourceAsStream("/sql/seed.sql")) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "seed.sql file not found."
                );
            }

            String sql;

            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         inputStream,
                                         StandardCharsets.UTF_8))) {

                sql = reader.lines()
                        .filter(line -> !line.trim().startsWith("--"))
                        .collect(Collectors.joining("\n"));
            }

            if (sql.isBlank()) {
                System.out.println("seed.sql is empty.");
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.executeUpdate();

                System.out.println(
                        "Seed data inserted successfully!"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to execute seed.sql",
                    e
            );
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        if (dataSource != null) {
            dataSource.close();
        }

        System.out.println(
                "Shakthi Mart database connection closed."
        );
    }

    public static Connection getConnection(
            ServletContext context)
            throws SQLException {

        HikariDataSource ds =
                (HikariDataSource) context
                        .getAttribute("dataSource");

        if (ds == null) {
            throw new SQLException(
                    "Database connection pool is not available."
            );
        }

        return ds.getConnection();
    }
}
