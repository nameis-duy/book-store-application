package com.example.bookstoreappliaction.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bookstoreappliaction.dao.*;
import com.example.bookstoreappliaction.models.*;
import com.example.bookstoreappliaction.utils.*;

@Database(entities = {User.class, Book.class, Genre.class, Order.class, OrderDetail.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class BookStoreDb extends RoomDatabase {
    public abstract UserDAO userDAO();
    public abstract BookDAO bookDAO();
    public abstract OrderDAO orderDAO();
    public abstract GenreDAO genreDAO ();
    public abstract OrderDetailDAO orderDetailDAO();
}