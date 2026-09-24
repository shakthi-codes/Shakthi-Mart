package dao;

import model.OrderItem;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAOImpl implements OrderItemDAO {

    private final ServletContext context;

    public OrderItemDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void save(OrderItem orderItem) {

        String sql = """
                INSERT INTO order_items
                (order_id, service_id, quantity, price)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderItem.getOrderId());
            statement.setInt(2, orderItem.getServiceId());
            statement.setInt(3, orderItem.getQuantity());
            statement.setDouble(4, orderItem.getPrice());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error saving order item", e);
        }
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) {

        List<OrderItem> items = new ArrayList<>();

        String sql = """
                SELECT id, order_id, service_id,
                       quantity, price
                FROM order_items
                WHERE order_id = ?
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet rs =
                         statement.executeQuery()) {

                while (rs.next()) {

                    items.add(
                            new OrderItem(
                                    rs.getInt("id"),
                                    rs.getInt("order_id"),
                                    rs.getInt("service_id"),
                                    rs.getInt("quantity"),
                                    rs.getDouble("price")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error finding order items", e);
        }

        return items;
    }
}
