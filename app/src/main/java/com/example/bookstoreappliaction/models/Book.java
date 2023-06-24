package com.example.bookstoreappliaction.models;

import java.util.Date;

public class Book {
    String title;
    int quantity;
    String imageUrl;
    float price;
    Date dataPublication;
    String authorName;
    int genreId;

    public Book(String title, int quantity, String imageUrl, float price, Date dataPublication, String authorName, int genreId) {
        this.title = title;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.price = price;
        this.dataPublication = dataPublication;
        this.authorName = authorName;
        this.genreId = genreId;
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

    public Date getDataPublication() {
        return dataPublication;
    }

    public void setDataPublication(Date dataPublication) {
        this.dataPublication = dataPublication;
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
