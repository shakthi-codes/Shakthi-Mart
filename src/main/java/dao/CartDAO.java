package dao;

import java.util.List;
import model.CartItem;

public interface CartDAO {

    void addToCart(int buyerId, int serviceId);

    List<CartItem> findByBuyerId(int buyerId);

    void removeFromCart(int buyerId, int serviceId);

    void clearCart(int buyerId);
}
