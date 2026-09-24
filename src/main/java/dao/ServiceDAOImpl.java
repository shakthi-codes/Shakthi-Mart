package dao;

import model.Service;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImpl implements ServiceDAO {

    private final ServletContext context;

    public ServiceDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void save(Service service) {

        String sql = """
                INSERT INTO services
                (creator_id, name, description, price, category)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, service.getCreatorId());
            statement.setString(2, service.getName());
            statement.setString(3, service.getDescription());
            statement.setDouble(4, service.getPrice());
            statement.setString(5, service.getCategory());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Unable to save service", e);
        }
    }

    @Override
    public List<Service> findAll() {

        String sql = """
                SELECT id, creator_id, name, description, price, category
                FROM services
                """;

        List<Service> services = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                services.add(mapService(resultSet));
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch services", e);
        }

        return services;
    }

    @Override
    public List<Service> findByCreatorId(int creatorId) {

        String sql = """
                SELECT id, creator_id, name, description, price, category
                FROM services
                WHERE creator_id = ?
                """;

        List<Service> services = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, creatorId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    services.add(mapService(resultSet));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch creator services", e);
        }

        return services;
    }

    @Override
    public Service findById(int id) {

        String sql = """
                SELECT id, creator_id, name, description, price, category
                FROM services
                WHERE id = ?
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapService(resultSet);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to find service", e);
        }

        return null;
    }

    @Override
    public void update(Service service) {

        String sql = """
                UPDATE services
                SET name = ?,
                    description = ?,
                    price = ?,
                    category = ?
                WHERE id = ?
                AND creator_id = ?
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());
            statement.setDouble(3, service.getPrice());
            statement.setString(4, service.getCategory());
            statement.setInt(5, service.getId());
            statement.setInt(6, service.getCreatorId());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Unable to update service", e);
        }
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM services WHERE id = ?";

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Unable to delete service", e);
        }
    }

    private Service mapService(ResultSet resultSet) throws Exception {

        return new Service(
                resultSet.getInt("id"),
                resultSet.getInt("creator_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getString("category")
        );
    }
}
