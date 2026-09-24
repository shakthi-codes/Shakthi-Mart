package dao;

import model.Order;

import java.util.List;

public interface OrderDAO {

    int createOrder(Order order);

    Order findById(int orderId);

    List<Order> findByBuyerId(int buyerId);

    void updateStatus(int orderId, String status);
}
