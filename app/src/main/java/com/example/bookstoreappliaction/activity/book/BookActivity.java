package com.example.bookstoreappliaction.activity.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.cart.CartActivity;
import com.example.bookstoreappliaction.activity.login.LoginActivity;
import com.example.bookstoreappliaction.activity.map.MapActivity;
import com.example.bookstoreappliaction.activity.orders.OrdersActivity;
import com.example.bookstoreappliaction.activity.users.UserProfile;
import com.example.bookstoreappliaction.adapter.ProductAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.example.bookstoreappliaction.models.User;

import java.util.List;

public class BookActivity extends AppCompatActivity {

    RecyclerView rvBooks;
    EditText etSearch;
    TextView tvBadge;
    ImageView imgCart;
    ImageButton btnHome, btnProfile, btnLocation, btnOrders;
    //
    List<Book> books;
    Order userCart;
    User loginUser;
    //
    BookStoreDb db;
    ProductAdapter adapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        //
        initDatabase();
        initUser();
        initView();
        //
        LoadList("");
        searchBook();
        btnProfile_OnClick();
        btnLocation_OnClick();
        btnOrders_OnClick();
    }

    void initUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(Constants.USER_ID, -1);
        if (userId == -1) {
            intent = new Intent(BookActivity.this, LoginActivity.class);
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
                                intent = new Intent(BookActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            });
        }
    }

    void initView() {
        rvBooks = findViewById(R.id.rvBookList);
        etSearch = findViewById(R.id.etSearch);
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

    public void initMenuViews() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                userCart = db.orderDAO().getCartByUserId(loginUser.getId());
                if (userCart != null) {
                    List<OrderDetail> cartDetails = db.orderDetailDAO().getDetailListByOrderId(userCart.getId());
                    int cartItemCount = cartDetails != null ? cartDetails.size() : 0;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvBadge.setText(String.format("%d", cartItemCount));
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvBadge.setText(String.format("%d", 0));
                        }
                    });
                }
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
    void btnProfile_OnClick() {
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(BookActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });
    }

    void btnLocation_OnClick() {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(BookActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    void btnOrders_OnClick() {
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(BookActivity.this, OrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void btnAddToCart_Click(int bookId) {
        addCart();
        addCartDetail(bookId);
        initMenuViews();
    }

    void addCart() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cartTmp = db.orderDAO().getCartByUserId(loginUser.getId());
                if (cartTmp == null) {
                    Order cart = new Order(loginUser.getId(), 0, false);
                    db.orderDAO().insert(cart);
                }
            }
        });
    }

    void addCartDetail(int bookId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cart = db.orderDAO().getCartByUserId(loginUser.getId());
                Book book = db.bookDAO().getBookById(bookId);
                if (cart != null) {
                    OrderDetail cartDetailTmp = db.orderDetailDAO().getDetailByCartIdAndBookId(cart.getId(), bookId);
                    if (cartDetailTmp == null) {
                        OrderDetail cartDetail = new OrderDetail(cart.getId(), bookId, 1, book.getPrice());
                        db.orderDetailDAO().insert(cartDetail);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookActivity.this, Constants.ADD_TO_CART_SUCCEED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (cartDetailTmp != null && cartDetailTmp.getQuantity() < book.getQuantity()){
                        int currentQuantity = cartDetailTmp.getQuantity();
                        cartDetailTmp.setQuantity(currentQuantity + 1);
                        db.orderDetailDAO().update(cartDetailTmp);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookActivity.this, Constants.ADD_TO_CART_SUCCEED, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookActivity.this, Constants.ADD_TO_CART_FAILED, Toast.LENGTH_SHORT).show();
                            }
                        });
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
                intent.putExtra(Constants.USER_CART_ID, userCart != null ? userCart.getId() : -1);
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