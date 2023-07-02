package com.example.bookstoreappliaction.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "user_id")
    int userId;
    @ColumnInfo(name = "order_date")
    Date orderDate;
    float total;
    @ColumnInfo(name = "is_paid")
    boolean isPaid;

    @Ignore
    public Order(int userId, float total, boolean isPaid) {
        this.userId = userId;
        this.total = total;
        this.isPaid = isPaid;
    }

    public Order(int userId, Date orderDate, float total, boolean isPaid) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.total = total;
        this.isPaid = isPaid;
    }

    @Ignore
    public Order(int id, int userId, Date orderDate, float total, boolean isPaid) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
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
