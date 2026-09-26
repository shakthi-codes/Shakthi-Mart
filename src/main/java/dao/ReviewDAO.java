package dao;

import model.Review;

import java.util.List;

public interface ReviewDAO {

    void save(Review review);

    List<Review> findByServiceId(int serviceId);

    double getAverageRating(int serviceId);
}
