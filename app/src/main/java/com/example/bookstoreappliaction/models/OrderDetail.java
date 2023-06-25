package com.example.bookstoreappliaction.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orderDetails")
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "order_id")
    int orderId;
    @ColumnInfo(name = "book_id")
    int bookId;
    int quantity;
    @ColumnInfo(name = "unit_price")
    float unitPrice;

    public OrderDetail(int id, int orderId, int bookId, int quantity, float unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
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

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }
}
