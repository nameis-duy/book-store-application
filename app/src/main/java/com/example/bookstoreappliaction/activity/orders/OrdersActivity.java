package com.example.bookstoreappliaction.activity.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.activity.login.LoginActivity;
import com.example.bookstoreappliaction.activity.map.MapActivity;
import com.example.bookstoreappliaction.activity.users.UserProfile;
import com.example.bookstoreappliaction.adapter.OrderAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.User;

import java.util.List;
import java.util.Objects;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView rvOrders;
    ImageButton btnHome, btnProfile, btnLocation, btnOrders;
    //
    BookStoreDb db;
    Intent intent;
    OrderAdapter adapter;
    //
    List<Order> orders;
    User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initDatabase();
        initView();
        initUser();
        //
        btnHome_OnClick();
        btnProfile_OnClick();
        btnLocation_OnClick();
    }

    void initUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(Constants.USER_ID, -1);
        if (userId == -1) {
            intent = new Intent(OrdersActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    loginUser = db.userDAO().getUserById(userId);
                    if (loginUser == null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                intent = new Intent(OrdersActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        LoadList();
                    }
                }
            });
        }
    }

    void initView() {
        rvOrders = findViewById(R.id.rvOrders);
        //
        btnHome = findViewById(R.id.btnHome);
        btnProfile = findViewById(R.id.btnProfile);
        btnLocation = findViewById(R.id.btnLocation);
        btnOrders = findViewById(R.id.btnOrders);
    }

    void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    void LoadList() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                orders = db.orderDAO().getOrdersByUserId(loginUser.getId());
                if (orders != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new OrderAdapter(orders);
                            rvOrders.setAdapter(adapter);
                            rvOrders.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
                        }
                    });
                }
            }
        });
    }
    //Event Handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent(OrdersActivity.this, BookActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void btnHome_OnClick() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OrdersActivity.this, BookActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void btnProfile_OnClick() {
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OrdersActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    void btnLocation_OnClick() {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OrdersActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}