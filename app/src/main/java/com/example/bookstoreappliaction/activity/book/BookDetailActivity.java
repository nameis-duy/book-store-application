package com.example.bookstoreappliaction.activity.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.cart.CartActivity;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Genre;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BookDetailActivity extends AppCompatActivity {

    ImageView imgBook, imgCart;
    TextView tvBookTitle, tvPrice, tvDateOfPublication, tvAuthor, tvGenre, tvBadge;
    EditText etQuantity;
    Button btnAddToCart;
    //
    BookStoreDb db;
    Order userCart;
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

    void initMenuViews() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                userCart = db.orderDAO().getCartByUserId(1);
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

    String formatDate(Date date) {
        SimpleDateFormat formatDisplay = new SimpleDateFormat("dd-MM-yyyy");

        return formatDisplay.format(date);
    }
    //Event Handler
    void btnAddToCart_OnClick() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            int bookId = getIntent().getIntExtra(Constants.BOOK_DETAIL_ID, -1);
            @Override
            public void onClick(View v) {
                addCart();
                addCartDetail(bookId);
                initMenuViews();
                Toast.makeText(BookDetailActivity.this, Constants.ADD_TO_CART_SUCCEED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addCart() {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cartTmp = db.orderDAO().getCartByUserId(1);
                if (cartTmp == null) {
                    Order cart = new Order(1, 0, false);
                    db.orderDAO().insert(cart);
                }
            }
        });
    }

    void addCartDetail(int bookId) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Order cart = db.orderDAO().getCartByUserId(1);
                int quantity = Integer.parseInt(etQuantity.getText().toString());
                if (cart != null) {
                    OrderDetail cartDetailTmp = db.orderDetailDAO().getDetailByCartIdAndBookId(cart.getId(), bookId);
                    if (cartDetailTmp == null) {
                        Book book = db.bookDAO().getBookById(bookId);
                        OrderDetail cartDetail = new OrderDetail(cart.getId(), bookId, quantity, book.getPrice());
                        db.orderDetailDAO().insert(cartDetail);
                    } else {
                        int currentQuantity = cartDetailTmp.getQuantity();
                        cartDetailTmp.setQuantity(currentQuantity + 1);
                        db.orderDetailDAO().update(cartDetailTmp);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
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
                intent = new Intent(BookDetailActivity.this, CartActivity.class);
                intent.putExtra(Constants.USER_CART_ID, userCart.getId());
                startActivity(intent);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}