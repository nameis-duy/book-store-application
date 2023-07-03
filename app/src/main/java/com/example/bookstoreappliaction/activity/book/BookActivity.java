package com.example.bookstoreappliaction.activity.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.cart.CartActivity;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;

import java.util.Date;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    RecyclerView rvBooks;
    EditText etSearch;
    TextView tvBadge;
    ImageView imgCart;
    //
    List<Book> books;
    Order userCart;
    //
    BookStoreDb db;
    ProductAdapter adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        //
        initView();
        initDatabase();
        //
        LoadList("");
        searchBook();
    }

    void initView() {
        rvBooks = findViewById(R.id.rvBookList);
        etSearch = findViewById(R.id.etSearch);
    }

    void initDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    public void initMenuViews() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                userCart = db.orderDAO().getCartByUserId(1);
                List<OrderDetail> cartDetails = db.orderDetailDAO().getDetailListByOrderId(userCart.getId());
                int cartItemCount = cartDetails != null ? cartDetails.size() : 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvBadge.setText(String.format("%d", cartItemCount));
                    }
                });
            }
        });
    }

    void LoadList(String search) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (search.length() == 0) {
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
                } else {
                    String searchPattern = "%" + search + "%";
                    books = db.bookDAO().getAllByTitle(searchPattern);
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
            }
        });
    }

    //Event Handler
    public void btnAddToCart_Click(int bookId) {
        addCart();
        addCartDetail(bookId);
        initMenuViews();
    }

    void addCart() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cartTmp = db.orderDAO().getCartByUserId(1);
                if (cartTmp == null) {
                    Order cart = new Order(1, 0, false);
                    db.orderDAO().insert(cart);
                    Log.d("msg", "Add Succeed");
                }
            }
        });
    }

    void addCartDetail(int bookId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cart = db.orderDAO().getCartByUserId(1);
                if (cart != null) {
                    OrderDetail cartDetailTmp = db.orderDetailDAO().getDetailByCartIdAndBookId(cart.getId(), bookId);
                    if (cartDetailTmp == null) {
                        Book book = db.bookDAO().getBookById(bookId);
                        OrderDetail cartDetail = new OrderDetail(cart.getId(), bookId, 1, book.getPrice());
                        db.orderDetailDAO().insert(cartDetail);
                        Log.d("msg", "Create Cart Detail Succeed");
                    } else {
                        int currentQuantity = cartDetailTmp.getQuantity();
                        cartDetailTmp.setQuantity(currentQuantity + 1);
                        db.orderDetailDAO().update(cartDetailTmp);
                    }
                }
            }
        });
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        //
        MenuItem item = menu.findItem(R.id.badge);
        MenuItemCompat.setActionView(item, R.layout.custom_cart);
        //
        RelativeLayout layout = (RelativeLayout) MenuItemCompat.getActionView(item);
        tvBadge = layout.findViewById(R.id.tvCartNotification);
        imgCart = layout.findViewById(R.id.imgCart);
        initMenuViews();
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(BookActivity.this, CartActivity.class);
                intent.putExtra(Constants.USER_CART_ID, userCart.getId());
                startActivity(intent);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initMenuViews();
    }
}