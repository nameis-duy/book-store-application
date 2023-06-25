package com.example.bookstoreappliaction.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bookstoreappliaction.models.Genre;

import java.util.List;

@Dao
public interface GenreDAO {
    @Query("SELECT * FROM genre")
    List<Genre> getAll();

    @Query("SELECT * FROM genre WHERE id IN (:genreId) LIMIT 1")
    Genre getGenreById(int genreId);

    @Insert
    void insert(Genre genre);

    @Update
    void update(Genre genre);

    @Delete
    void delete(Genre genre);
}
