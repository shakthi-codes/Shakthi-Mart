package model;

public class OrderItem {

    private int id;
    private int orderId;
    private int serviceId;
    private int quantity;
    private double price;

    public OrderItem() {
    }

    public OrderItem(int id, int orderId, int serviceId,
                     int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
