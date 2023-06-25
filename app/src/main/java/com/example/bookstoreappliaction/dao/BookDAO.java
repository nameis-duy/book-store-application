package com.example.bookstoreappliaction.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstoreappliaction.models.Book;

import java.util.List;

@Dao
public interface BookDAO {

    @Query("SELECT * FROM books")
    List<Book> getAll();

    @Query("SELECT * FROM books WHERE id IN (:bookId) LIMIT 1")
    Book getBookById(int bookId);

    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);
}
