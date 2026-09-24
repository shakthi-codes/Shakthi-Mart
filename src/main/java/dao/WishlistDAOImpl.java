package dao;

import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WishlistDAOImpl implements WishlistDAO {

    private final ServletContext context;

    public WishlistDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void addToWishlist(int buyerId, int serviceId) {

        String sql = """
                INSERT INTO wishlist_items
                (buyer_id, service_id)
                VALUES (?, ?)
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);
            statement.setInt(2, serviceId);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to add service to wishlist", e
            );
        }
    }

    @Override
    public void removeFromWishlist(int buyerId, int serviceId) {

        String sql = """
                DELETE FROM wishlist_items
                WHERE buyer_id = ?
                AND service_id = ?
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);
            statement.setInt(2, serviceId);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to remove service from wishlist", e
            );
        }
    }

    @Override
    public boolean isInWishlist(int buyerId, int serviceId) {

        String sql = """
                SELECT id
                FROM wishlist_items
                WHERE buyer_id = ?
                AND service_id = ?
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, buyerId);
            statement.setInt(2, serviceId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to check wishlist", e
            );
        }
    }

    @Override
    public int getWishlistCount(int serviceId) {

        String sql = """
                SELECT COUNT(*)
                FROM wishlist_items
                WHERE service_id = ?
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, serviceId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to get wishlist count", e
            );
        }

        return 0;
    }
}
