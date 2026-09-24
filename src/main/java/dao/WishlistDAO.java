package dao;

public interface WishlistDAO {

    void addToWishlist(int buyerId, int serviceId);

    void removeFromWishlist(int buyerId, int serviceId);

    boolean isInWishlist(int buyerId, int serviceId);

    int getWishlistCount(int serviceId);
}
