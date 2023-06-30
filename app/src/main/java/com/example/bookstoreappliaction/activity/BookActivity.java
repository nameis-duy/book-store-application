package com.example.bookstoreappliaction.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Genre;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        Button btnCreate = findViewById(R.id.btnCreateProduct);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        String someDate = "2022-10-05";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = null;
                        try {
                            date = sdf.parse(someDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(date.getTime());
                        Book book = new Book("Amazing Spider-Man: Facsimile Edition (2022) #1",
                                10,
                                "https://cdn.marvel.com/u/prod/marvel/i/mg/c/80/633cefe9900ea/clean.jpg",
                                3.99f,
                                date,
                                "Stan Lee and Steve Ditko",
                                1);

                        db.bookDAO().insert(book);

                        Genre genre = new Genre("Superhero");
                        db.genreDAO().insert(genre);
                        Log.d("msg", "Succeed");
                    }
                });
            }
        });
        LoadList();
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ProductAdapter(books);
                            rvBooks.setAdapter(adapter);
                            rvBooks.setLayoutManager(new LinearLayoutManager(BookActivity.this));
                        }
                    });
                }
            }
        });
    }
    //Event Handler
}