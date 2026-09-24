package dao;

import model.Order;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private final ServletContext context;

    public OrderDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public int createOrder(Order order) {

        String sql = """
                INSERT INTO orders
                (buyer_id, total_amount, status)
                VALUES (?, ?, ?)
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getBuyerId());
            statement.setDouble(2, order.getTotalAmount());
            statement.setString(3, order.getStatus());

            statement.executeUpdate();

            try (ResultSet rs =
                         statement.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error creating order", e);
        }

        return 0;
    }

    @Override
    public Order findById(int orderId) {

        String sql = """
                SELECT id, buyer_id, total_amount,
                       status, created_at
                FROM orders
                WHERE id = ?
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet rs =
                         statement.executeQuery()) {

                if (rs.next()) {

                    return new Order(
                            rs.getInt("id"),
                            rs.getInt("buyer_id"),
                            rs.getDouble("total_amount"),
                            rs.getString("status"),
                            rs.getTimestamp("created_at")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding order", e);
        }

        return null;
    }

    @Override
    public List<Order> findByBuyerId(int buyerId) {

        List<Order> orders = new ArrayList<>();

        String sql = """
                SELECT id, buyer_id, total_amount,
                       status, created_at
                FROM orders
                WHERE buyer_id = ?
                ORDER BY created_at DESC
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);

            try (ResultSet rs =
                         statement.executeQuery()) {

                while (rs.next()) {

                    orders.add(
                            new Order(
                                    rs.getInt("id"),
                                    rs.getInt("buyer_id"),
                                    rs.getDouble("total_amount"),
                                    rs.getString("status"),
                                    rs.getTimestamp("created_at")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding buyer orders", e);
        }

        return orders;
    }

    @Override
    public void updateStatus(
            int orderId,
            String status) {

        String sql = """
                UPDATE orders
                SET status = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setInt(2, orderId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error updating order status", e);
        }
    }
}
