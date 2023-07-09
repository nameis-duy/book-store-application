package com.example.bookstoreappliaction.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.activity.login.LoginActivity;
import com.example.bookstoreappliaction.activity.map.MapActivity;
import com.example.bookstoreappliaction.adapter.AdminProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Genre;

import java.util.List;

public class AdminBookListActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvBooks;
    EditText etSearch;
    Button btnCreate;
    ImageButton btnDashboard, btnUsers, btnLogOut;
    //
    List<Book> books;
    BookStoreDb db;
    AdminProductAdapter adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_list);

        initDatabase();
        initView();
        LoadList("");
        searchBook();
        btnCreate_onClick();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initDatabase();
        initView();
        LoadList("");
        searchBook();
        btnCreate_onClick();
    }

    void initView() {
        rvBooks = findViewById(R.id.activity_adminbooklist_rv_bookList);
        etSearch = findViewById(R.id.activity_adminbooklist_et_search);
        btnCreate = findViewById(R.id.btnCreate);

        btnDashboard = findViewById(R.id.btnDashboard);
        btnUsers = findViewById(R.id.btnUsers);
        btnLogOut = findViewById(R.id.btnLogout);

        btnDashboard.setOnClickListener(this);
        btnUsers.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.genreDAO().insert(new Genre("Comic Book"));
            }
        });
    }

    void btnCreate_onClick() {
        btnCreate.setOnClickListener(v -> {
            intent = new Intent(AdminBookListActivity.this, CreateProductActivity.class);
            startActivity(intent);
        });
    }

    void LoadList(String search) {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            if (search.length() == 0) {
                books = db.bookDAO().getAll();
                if (books != null) {
                    runOnUiThread(() -> {
                        adapter = new AdminProductAdapter(books);
                        rvBooks.setAdapter(adapter);
                        rvBooks.setLayoutManager(new LinearLayoutManager(AdminBookListActivity.this));
                    });
                }
            }
            else {
                String searchPattern = "%" + search + "%";
                books = db.bookDAO().getAllByTitle(searchPattern);
                if (books != null) {
                    runOnUiThread(() -> {
                        adapter = new AdminProductAdapter(books);
                        rvBooks.setAdapter(adapter);
                        rvBooks.setLayoutManager(new LinearLayoutManager(AdminBookListActivity.this));
                    });
                }
            }
        });
    }
    //Event Handler
    void searchBook() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchValue = etSearch.getText().toString();
                LoadList(searchValue);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnDashboard){
            intent = new Intent(AdminBookListActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btnUsers){
            intent = new Intent(AdminBookListActivity.this, AdminUserListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnLogout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        intent = new Intent(AdminBookListActivity.this, LoginActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}