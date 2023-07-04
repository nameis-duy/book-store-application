package com.example.bookstoreappliaction.activity.orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.activity.login.LoginActivity;
import com.example.bookstoreappliaction.adapter.OrderDetailAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.example.bookstoreappliaction.models.User;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.List;
import java.util.Objects;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView tvTitle, tvTotal, tvItemCount;
    RecyclerView rvOrderDetails;
    //
    BookStoreDb db;
    Intent intent;
    OrderDetailAdapter adapter;
    //
    List<OrderDetail> details;
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initView();
        initDb();
        orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
        if (orderId == -1) {
            intent = new Intent();
            finish();
        }
        //
        loadList();
    }

    void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTotal = findViewById(R.id.tvTotal);
        tvItemCount = findViewById(R.id.tvItemCount);
        //
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
    }

    @SuppressLint("DefaultLocale")
    void initViewValue() {
        tvTitle.setText(String.format("Order #%d details", getIntent().getIntExtra(Constants.ORDER_ID, -1)));
        float total = getIntent().getFloatExtra(Constants.ORDER_TOTAL, -1);
        if (total != -1) {
            tvTotal.setText(String.format("Total: $%.2f", total));
        }
        tvItemCount.setText(String.format("%d items", details.size()));
    }

    void initDb() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    void loadList() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                details = db.orderDetailDAO().getDetailListByOrderId(orderId);
                if (details != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new OrderDetailAdapter(details);
                            rvOrderDetails.setAdapter(adapter);
                            rvOrderDetails.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));
                            initViewValue();
                        }
                    });
                } else {
                    intent = new Intent();
                    finish();
                }
            }
        });
    }

    public BookStoreDb getDb() { return db; }
    //Event Handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}