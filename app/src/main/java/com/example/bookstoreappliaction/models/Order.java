package com.example.bookstoreappliaction.models;

public class Order {
    int id;
    int userId;
    int orderDetailId;
    float total;
    boolean isPaid;

    public Order(int id, int userId, int orderDetailId, float total, boolean isPaid) {
        this.id = id;
        this.userId = userId;
        this.orderDetailId = orderDetailId;
        this.total = total;
        this.isPaid = isPaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
