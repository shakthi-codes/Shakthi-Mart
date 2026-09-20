package dao;

import model.User;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private final ServletContext context;

    public UserDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void save(User user) {

        String sql = """
                INSERT INTO users (name, email, password, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Unable to save user", e);
        }
    }

    @Override
    public User findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to find user", e);
        }

        return null;
    }

    @Override
    public User findById(int id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to find user", e);
        }

        return null;
    }

    @Override
    public List<User> findAll() {

        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch users", e);
        }

        return users;
    }
}