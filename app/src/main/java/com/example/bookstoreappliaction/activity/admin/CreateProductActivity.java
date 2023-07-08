package com.example.bookstoreappliaction.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Genre;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateProductActivity extends AppCompatActivity {
    private EditText etTitle, etQuantity, etImage, etPrice, etAuthor;
    private Spinner sGenre;
    private Button btnSave;
    private List<Genre> genres;
    private BookStoreDb db;
    private Bundle extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_createproduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDatabase();
        initViews();
        populateGenreSpinner();
        populateUI();
    }

    private void initViews() {
        etTitle = findViewById(R.id.etCreateTitle);
        etQuantity = findViewById(R.id.etCreateQuantity);
        etImage = findViewById(R.id.etProductImage);
        etPrice = findViewById(R.id.etCreatePrice);
        etAuthor = findViewById(R.id.etCreateAuthor);
        sGenre = findViewById(R.id.sCreateGenre);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSave_onClick();
            }
        });
    }

    void populateUI(){
        extras = getIntent().getExtras();
        if(extras!=null){
            int updateId = extras.getInt("UpdateId");
            AppExecutors.getInstance().getDiskIO().execute(() -> {
                Book updateBook = db.bookDAO().getBookById(updateId);
                if(updateBook!=null){
                    runOnUiThread(() -> {
                        etTitle.setText(updateBook.getTitle());
                        etQuantity.setText(Integer.toString(updateBook.getQuantity()));
                        etImage.setText(updateBook.getImageUrl());
                        etPrice.setText(Float.toString(updateBook.getPrice()));
                        etAuthor.setText(updateBook.getAuthorName());
                    });
                }
            });
        }
    }

    void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    boolean validateInput() {
        String txtTitle = etTitle.getText().toString();
        String txtQuantity = etQuantity.getText().toString();
        String txtPrice = etPrice.getText().toString();
        String txtAuthor = etAuthor.getText().toString();

        if (TextUtils.isEmpty(txtTitle)) {
            etTitle.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(txtQuantity)) {
            etQuantity.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(txtPrice)) {
            etPrice.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(txtAuthor)) {
            etAuthor.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        return true;
    }

    void populateGenreSpinner(){
        AppExecutors.getInstance().getDiskIO().execute(() -> {
                genres = db.genreDAO().getAll();
                if (genres != null) {
                    runOnUiThread(() -> {
                        List<String> genreNames = new ArrayList<String>();
                        for(Genre item : genres){
                            genreNames.add(item.getName());
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, genreNames);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sGenre.setAdapter(arrayAdapter);
                    });
                }
        });
    }



    public void btnSave_onClick() {
        if(validateInput()){
            Date currentDate = Calendar.getInstance().getTime();
            int genreId = 0;
            for(Genre item : genres){
                if(item.getName().equals(sGenre.getSelectedItem().toString())){
                    genreId = item.getId();
                }
            }
            Book book = new Book(
                    etTitle.getText().toString(),
                    Integer.parseInt(etQuantity.getText().toString()),
                    etImage.getText().toString(),
                    Float.parseFloat(etPrice.getText().toString()),
                    currentDate,
                    etAuthor.getText().toString(),
                    genreId);
            Bundle extras = getIntent().getExtras();
            if(extras!=null){
                book.setId(extras.getInt("UpdateId"));
                AppExecutors.getInstance().getDiskIO().execute(() -> {
                    db.bookDAO().update(book);
                });
            }else{
                AppExecutors.getInstance().getDiskIO().execute(() -> {
                    db.bookDAO().insert(book);
                });
            }
            finish();
        }
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
