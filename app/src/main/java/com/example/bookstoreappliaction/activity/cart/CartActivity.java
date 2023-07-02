package com.example.bookstoreappliaction.activity.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.adapter.CartAdapter;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;

import java.util.List;
import java.util.Objects;

public class CartActivity extends AppCompatActivity {

    RecyclerView rvCart;
    TextView tvTotal, tvCartCount;
    Button btnCheckout;
    //
    List<OrderDetail> carts;
    CartAdapter adapter;
    BookStoreDb db;
    //
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initViews();
        initDb();
        //
        LoadList();
        //
    }

    void initViews() {
        rvCart = findViewById(R.id.rvCart);
        //
        tvTotal = findViewById(R.id.tvTotal);
        tvCartCount = findViewById(R.id.tvItemCount);
        //
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    void initViewValues() {
        tvCartCount.setText(String.format("%d items", carts == null ? 0 : carts.size()));
        float total = GenerateTotal(carts);
        tvTotal.setText(String.format("Total: $%.02f", total));
    }

    void initDb() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    void LoadList() {
        int cartId = getIntent().getIntExtra(Constants.USER_CART_ID, -1);
        if (cartId != -1) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    carts = db.orderDetailDAO().getDetailListByOrderId(cartId);
                    if (carts != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new CartAdapter(carts);
                                rvCart.setAdapter(adapter);
                                rvCart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                                initViewValues();
                            }
                        });
                    }
                }
            });
        } else  {
            intent = new Intent();
            finish();
        }
    }

    float GenerateTotal(List<OrderDetail> details) {
        float total = 0;
        for (OrderDetail detail : details) {
            total += detail.getQuantity() * detail.getUnitPrice();
        }
        return total;
    }

    public BookStoreDb getDB() {
        return db;
    }
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