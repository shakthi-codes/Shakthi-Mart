package dao;

import model.Review;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class ReviewDAOImpl implements ReviewDAO {

    private final ServletContext context;

    public ReviewDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void save(Review review) {

        String sql = """
                INSERT INTO reviews
                (buyer_id, service_id, rating, comment)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, review.getBuyerId());
            statement.setInt(2, review.getServiceId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getComment());

            statement.executeUpdate();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to save review",
                    e
            );
        }
    }

    @Override
    public List<Review> findByServiceId(int serviceId) {

        String sql = """
                SELECT id,
                       buyer_id,
                       service_id,
                       rating,
                       comment
                FROM reviews
                WHERE service_id = ?
                ORDER BY created_at DESC
                """;

        List<Review> reviews = new ArrayList<>();

        try (Connection connection =
                     DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, serviceId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Review review = new Review();

                    review.setId(
                            resultSet.getInt("id")
                    );

                    review.setBuyerId(
                            resultSet.getInt("buyer_id")
                    );

                    review.setServiceId(
                            resultSet.getInt("service_id")
                    );

                    review.setRating(
                            resultSet.getInt("rating")
                    );

                    review.setComment(
                            resultSet.getString("comment")
                    );

                    reviews.add(review);
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to fetch reviews",
                    e
            );
        }

        return reviews;
    }

    @Override
    public double getAverageRating(int serviceId) {

        String sql = """
                SELECT AVG(rating)
                FROM reviews
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

                    double average =
                            resultSet.getDouble(1);

                    if (resultSet.wasNull()) {
                        return 0.0;
                    }

                    return average;
                }
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to calculate average rating",
                    e
            );
        }

        return 0.0;
    }
}
