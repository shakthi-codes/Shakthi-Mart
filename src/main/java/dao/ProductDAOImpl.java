package dao;

import model.Product;
import util.DBUtil;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private final ServletContext context;

    public ProductDAOImpl(ServletContext context) {
        this.context = context;
    }

    @Override
    public void save(Product product) {

        String sql = """
                INSERT INTO products
                (seller_id, name, description, price, stock_qty, category, image_url)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, product.getSellerId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setInt(5, product.getStockQty());
            statement.setString(6, product.getCategory());
            statement.setString(7, product.getImageUrl());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Unable to save product", e);
        }
    }

    @Override
    public List<Product> findAll() {

        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch products", e);
        }

        return products;
    }

    @Override
    public List<Product> findBySellerId(int sellerId) {

        String sql = "SELECT * FROM products WHERE seller_id = ?";
        List<Product> products = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, sellerId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to fetch seller products", e);
        }

        return products;
    }

    @Override
    public Product findById(int id) {

        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to find product", e);
        }

        return null;
    }

    @Override
    public void update(Product product) {

        String sql = """
                UPDATE products
                SET name = ?, description = ?, price = ?,
                    stock_qty = ?, category = ?, image_url = ?
                WHERE id = ? AND seller_id = ?
                """;

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQty());
            statement.setString(5, product.getCategory());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getId());
            statement.setInt(8, product.getSellerId());

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to update product", e);
        }
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection connection = DBUtil.getConnection(context);
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to delete product", e);
        }
    }

    private Product mapProduct(ResultSet resultSet)
            throws Exception {

        return new Product(
                resultSet.getInt("id"),
                resultSet.getInt("seller_id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDouble("price"),
                resultSet.getInt("stock_qty"),
                resultSet.getString("category"),
                resultSet.getString("image_url")
        );
    }
}
