package com.example.bookstoreappliaction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;

import java.util.List;

public class BookActivity extends AppCompatActivity {

    RecyclerView rvBooks;
    EditText etSearch;
    //
    List<Book> books;
    BookStoreDb db;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        //
        initView();
        initDatabase();
        //
    }

    void initView() {
        rvBooks = findViewById(R.id.rvBookList);
        etSearch = findViewById(R.id.etSearch);
    }

    void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    void LoadList() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                books = db.bookDAO().getAll();
                if (books != null) {
                    adapter = new ProductAdapter(books);
                    rvBooks.setAdapter(adapter);
                    rvBooks.setLayoutManager(new LinearLayoutManager(BookActivity.this));
                }
            }
        });
    }
    //Event Handler
}