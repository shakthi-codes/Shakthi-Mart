package dao;

import model.Product;
import java.util.List;

public interface ProductDAO {

    void save(Product product);

    List<Product> findAll();

    List<Product> findBySellerId(int sellerId);

    Product findById(int id);

    void update(Product product);

    void delete(int id);
}
