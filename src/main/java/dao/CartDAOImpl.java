package dao;

import model.CartItem;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    private final ServletContext context;

    public CartDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void addToCart(int buyerId, int serviceId) {

        String checkSql = """
                SELECT quantity
                FROM cart_items
                WHERE buyer_id = ? AND service_id = ?
                """;

        String insertSql = """
                INSERT INTO cart_items (buyer_id, service_id, quantity)
                VALUES (?, ?, 1)
                """;

        String updateSql = """
                UPDATE cart_items
                SET quantity = quantity + 1
                WHERE buyer_id = ? AND service_id = ?
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement checkStatement =
                     connection.prepareStatement(checkSql)) {

            checkStatement.setInt(1, buyerId);
            checkStatement.setInt(2, serviceId);

            try (ResultSet rs = checkStatement.executeQuery()) {

                if (rs.next()) {

                    try (PreparedStatement updateStatement =
                                 connection.prepareStatement(updateSql)) {

                        updateStatement.setInt(1, buyerId);
                        updateStatement.setInt(2, serviceId);
                        updateStatement.executeUpdate();
                    }

                } else {

                    try (PreparedStatement insertStatement =
                                 connection.prepareStatement(insertSql)) {

                        insertStatement.setInt(1, buyerId);
                        insertStatement.setInt(2, serviceId);
                        insertStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error adding service to cart", e);
        }
    }

    @Override
    public List<CartItem> findByBuyerId(int buyerId) {

        List<CartItem> cartItems = new ArrayList<>();

        String sql = """
                SELECT c.id,
                       c.buyer_id,
                       c.service_id,
                       c.quantity,
                       s.name,
                       s.price
                FROM cart_items c
                JOIN services s ON c.service_id = s.id
                WHERE c.buyer_id = ?
                ORDER BY c.created_at DESC
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    CartItem item = new CartItem(
                            rs.getInt("id"),
                            rs.getInt("buyer_id"),
                            rs.getInt("service_id"),
                            rs.getInt("quantity"),
                            rs.getString("name"),
                            rs.getDouble("price")
                    );

                    cartItems.add(item);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error loading cart", e);
        }

        return cartItems;
    }

    @Override
    public void removeFromCart(int buyerId, int serviceId) {

        String sql = """
                DELETE FROM cart_items
                WHERE buyer_id = ? AND service_id = ?
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);
            statement.setInt(2, serviceId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error removing item from cart", e);
        }
    }

    @Override
    public void clearCart(int buyerId) {

        String sql = """
                DELETE FROM cart_items
                WHERE buyer_id = ?
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error clearing cart", e);
        }
    }
}
