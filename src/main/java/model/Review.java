package model;

public class Review {

    private int id;
    private int buyerId;
    private int serviceId;
    private int rating;
    private String comment;

    public Review() {
    }

    public Review(
            int id,
            int buyerId,
            int serviceId,
            int rating,
            String comment) {

        this.id = id;
        this.buyerId = buyerId;
        this.serviceId = serviceId;
        this.rating = rating;
        this.comment = comment;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
