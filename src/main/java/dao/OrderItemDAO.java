package dao;

import model.OrderItem;

import java.util.List;

public interface OrderItemDAO {

    void save(OrderItem orderItem);

    List<OrderItem> findByOrderId(int orderId);
}
