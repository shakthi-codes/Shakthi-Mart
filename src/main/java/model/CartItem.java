package model;

public class CartItem {

    private int id;
    private int buyerId;
    private int serviceId;
    private int quantity;

    private String serviceName;
    private double price;

    public CartItem() {
    }

    public CartItem(int id, int buyerId, int serviceId, int quantity,
                    String serviceName, double price) {
        this.id = id;
        this.buyerId = buyerId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.serviceName = serviceName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
