package com.example.bookstoreappliaction.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "genre")
public class Genre {
    @PrimaryKey(autoGenerate = true)
    int id;
    String name;

    public Genre(String name) {
        this.name = name;
    }

    @Ignore
    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
