package com.example.bookstoreappliaction.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Genre;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateProductActivity extends AppCompatActivity {
    private EditText etTitle, etQuantity, etImage, etPrice, etAuthor;
    private Spinner etGenre;
    private Button Save;
    private int ProductId, GenreId;
    private Intent intent;
    private BookStoreDb bDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createproduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        bDb = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();

        intent = getIntent();
        if (intent != null && intent.hasExtra("ProductId")) {
            Save.setText("Update");
            ProductId = intent.getIntExtra("ProductId", -1);
            GenreId = intent.getIntExtra("genreId", -1);

            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Book book = bDb.bookDAO().getBookById(ProductId);
                    Genre genre = bDb.genreDAO().getGenreById(GenreId);
                    populateUI(book, genre);
                }
            });
        }
    }

    private void populateUI(Book book, Genre genre) {
        if(book == null){
            return;
        }

        etTitle.setText(book.getTitle());
        int quantity = book.getQuantity();
        String quantityString = String.valueOf(quantity);
        etQuantity.setText(quantityString);
        etImage.setText(book.getImageUrl());
        float price = book.getPrice();
        String priceString = String.valueOf(price);
        etPrice.setText(priceString);
        etAuthor.setText(book.getAuthorName());
        // Fetch all genres from GenreDAO and set them as options in the Spinner
        List<Genre> genres = bDb.genreDAO().getAll();
        ArrayAdapter<Genre> genreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genres);
        etGenre.setAdapter(genreAdapter);

        // Set the selected genre in the Spinner
        int genreIndex = genreAdapter.getPosition(genre);
        etGenre.setSelection(genreIndex);
    }

    private void initViews() {
        etTitle = findViewById(R.id.etProductTitle);
        etQuantity = findViewById(R.id.etProductQuantity);
        etImage = findViewById(R.id.etProductImage);
        etPrice = findViewById(R.id.etProductPrice);
        etAuthor = findViewById(R.id.etProductAuthor);
        etGenre = findViewById(R.id.etProductGenre);

        Save = findViewById(R.id.btnSave);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    public void onSaveButtonClicked() {
        Date currentDate = Calendar.getInstance().getTime();

        final Book book = new Book(
                etTitle.getText().toString(),
                Integer.parseInt(etQuantity.getText().toString()),
                etImage.getText().toString(),
                Float.parseFloat(etPrice.getText().toString()),
                currentDate,
                etAuthor.getText().toString(),
                ((Genre) etGenre.getSelectedItem()).getId());
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!intent.hasExtra("ProductId")) {
                    bDb.bookDAO().insert(book);
                } else {
                    book.setId(ProductId);
                    bDb.bookDAO().update(book);
                }
                finish();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
