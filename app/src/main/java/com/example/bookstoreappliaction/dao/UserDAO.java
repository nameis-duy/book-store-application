package com.example.bookstoreappliaction.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstoreappliaction.models.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id IN (:userId) LIMIT 1")
    User getUserById(int userId);

    @Query("SELECT * FROM users WHERE phone IN (:phone) LIMIT 1")
    User getUserByPhone(String phone);

    @Query("SELECT * FROM users WHERE phone IN (:phone) AND password IN (:password) LIMIT 1")
    User getUserByPhoneAndPassword(String phone, String password);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
