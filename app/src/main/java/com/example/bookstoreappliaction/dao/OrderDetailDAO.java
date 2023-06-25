package com.example.bookstoreappliaction.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.bookstoreappliaction.models.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDAO {
    @Query("SELECT * FROM orderDetails")
    List<OrderDetail> getAll();

    @Query("SELECT * FROM orderDetails WHERE id IN (:detailId) LIMIT 1")
    OrderDetail getById(int detailId);

    @Insert
    void insert(OrderDetail detail);

    @Update
    void update(OrderDetail detail);

    @Delete
    void delete(OrderDetail detail);
}
