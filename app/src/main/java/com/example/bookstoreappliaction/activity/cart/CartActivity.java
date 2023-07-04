package com.example.bookstoreappliaction.activity.cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.activity.payment.PaymentActivity;
import com.example.bookstoreappliaction.adapter.CartAdapter;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.example.bookstoreappliaction.notification_config.NotificationConfig;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.util.Date;
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
        btnCheckout_OnClick();
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

    public void LoadList() {
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
        }
    }

    float GenerateTotal(List<OrderDetail> details) {
        float total = 0;
        for (OrderDetail detail : details) {
            total += detail.getQuantity() * detail.getUnitPrice();
        }
        return total;
    }

    void updateBookQuantity(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Book book = db.bookDAO().getBookById(detail.getBookId());
                    book.setQuantity(book.getQuantity() - detail.getQuantity());
                    db.bookDAO().update(book);
                }
            });
        }
    }

    void sendNotification(String title, String message, int userId) {
        NotificationManager manager = NotificationConfig.getNotificationManger(this);
        Notification.Builder builder = NotificationConfig.getBuilder(this, title, message);
        Notification notification = builder.build();
        if (manager != null) {
            manager.notify(userId, notification);
        }
    }

    public BookStoreDb getDB() {
        return db;
    }

    public TextView getTvTotal() {
        return tvTotal;
    }

    //Event Handler
    void btnCheckout_OnClick() {
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cartId = getIntent().getIntExtra(Constants.USER_CART_ID, -1);
                if (cartId != -1) {
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Order cart = db.orderDAO().getCartById(cartId);
                            //check if cart is null
                            if (cart != null) {
                                List<OrderDetail> carts = db.orderDetailDAO().getDetailListByOrderId(cartId);
                                float total = GenerateTotal(carts);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        intent = new Intent(CartActivity.this, PaymentActivity.class);
                                        intent.putExtra(Constants.ORDER_TOTAL, total);
                                        startActivityForResult(intent, 99);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(CartActivity.this, Constants.CHECKOUT_ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void finishCheckout() {
        int cartId = getIntent().getIntExtra(Constants.USER_CART_ID, -1);
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cart = db.orderDAO().getCartById(cartId);
                //check if cart is null
                if (cart != null) {
                    List<OrderDetail> carts = db.orderDetailDAO().getDetailListByOrderId(cartId);
                    float total = GenerateTotal(carts);
                    //update book quantity if payment succeed
                    updateBookQuantity(carts);
                    //update cart if payment succeed
                    cart.setPaid(true);
                    cart.setOrderDate(new Date());
                    cart.setTotal(total);
                    db.orderDAO().update(cart);
                    runOnUiThread(new Runnable() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void run() {
                            //notify if payment succeed
                            sendNotification(Constants.APP_NAME,
                                    String.format(Constants.NOTIFICATION_AFTER_CHECKOUT_SUCCEED, carts.size()),
                                    cart.getUserId());
                            intent = new Intent();
                            Toast.makeText(CartActivity.this, Constants.CHECKOUT_SUCCEED, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && data != null) {
            if (data.getStringExtra(Constants.PAYMENT_MESSAGE).equals(Constants.PAYMENT_SUCCEED)) {
                finishCheckout();
            } else {
                Toast.makeText(CartActivity.this, Constants.CHECKOUT_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}