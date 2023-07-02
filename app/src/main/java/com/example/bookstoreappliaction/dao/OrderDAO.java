package com.example.bookstoreappliaction.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstoreappliaction.models.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM orders")
    List<Order> getAll();

    @Query("SELECT * FROM orders WHERE id IN (:orderId) LIMIT 1")
    Order getById(int orderId);

    @Query("SELECT * FROM orders WHERE is_paid = 0 AND user_id = (:userId)")
    List<Order> getCartListByUserId(int userId);

    @Query("SELECT * FROM orders WHERE is_paid = 0 AND user_id = (:userId) LIMIT 1")
    Order getCartByUserId(int userId);

    @Insert
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);
}
