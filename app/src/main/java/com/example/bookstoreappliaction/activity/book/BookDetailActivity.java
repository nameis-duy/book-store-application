package com.example.bookstoreappliaction.activity.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Genre;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class BookDetailActivity extends AppCompatActivity {

    ImageView imgBook;
    TextView tvBookTitle, tvPrice, tvDateOfPublication, tvAuthor, tvGenre;
    EditText etQuantity;
    Button btnAddToCart;
    //
    BookStoreDb db;
    //
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initDB();
        initView();
        //
    }

    void initView() {
        imgBook = findViewById(R.id.imgDetail);
        //
        tvBookTitle = findViewById(R.id.tvBookTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvDateOfPublication = findViewById(R.id.tvDateOfPublication);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvGenre = findViewById(R.id.tvGenre);
        //
        etQuantity = findViewById(R.id.etQuantity);
        //
        btnAddToCart = findViewById(R.id.btnAddToCart);
        //
        int bookId = getIntent().getIntExtra(Constants.BOOK_DETAIL_ID, -1);
        if (bookId != -1) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Book book = db.bookDAO().getBookById(bookId);
                    if (book != null) {
                        Genre bookGenre = db.genreDAO().getGenreById(book.getGenreId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get().load(book.getImageUrl()).into(imgBook);
                                tvBookTitle.setText(book.getTitle());
                                tvPrice.setText("$" + book.getPrice());
                                tvDateOfPublication.setText("Date publication: " + formatDate(book.getDatePublication()));
                                tvAuthor.setText("Author: " + book.getAuthorName());
                                tvGenre.setText(bookGenre == null ? "Genre: Super-Hero" : "Genre: " + bookGenre.getName());
                            }
                        });
                    }
                }
            });
        }
        else {
            intent = new Intent();
            finish();
        }
    }

    void initDB() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    String formatDate(Date date) {
        SimpleDateFormat formatDisplay = new SimpleDateFormat("dd-MM-yyyy");

        return formatDisplay.format(date);
    }
    //

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}