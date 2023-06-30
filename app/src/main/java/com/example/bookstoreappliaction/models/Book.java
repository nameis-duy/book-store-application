package com.example.bookstoreappliaction.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    int quantity;
    @ColumnInfo(name = "image_url")
    String imageUrl;
    float price;
    @ColumnInfo(name = "date_publication")
    Date datePublication;
    @ColumnInfo(name = "author_name")
    String authorName;

    @ColumnInfo(name = "genre_id")
    int genreId;

    public Book(String title, int quantity, String imageUrl, float price, Date datePublication, String authorName, int genreId) {
        this.title = title;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.price = price;
        this.datePublication = datePublication;
        this.authorName = authorName;
        this.genreId = genreId;
    }

    @Ignore
    public Book(int id, String title, int quantity, String imageUrl, float price, Date datePublication, String authorName, int genreId) {
        this.id = id;
        this.title = title;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.price = price;
        this.datePublication = datePublication;
        this.authorName = authorName;
        this.genreId = genreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Date datePublication) {
        this.datePublication = datePublication;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}
