package com.example.bookstoreappliaction.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.admin.AdminDashboardActivity;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.example.bookstoreappliaction.models.User;
import com.example.bookstoreappliaction.notification_config.NotificationConfig;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText etPhone, etPassword;
    Button btnLogin;
    TextView tvNewAccount;
    //
    BookStoreDb db;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initViews();
        initDb();
        //
        buttonLogin_Click();
        NewAccount_Click();
    }

    void initDb() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvNewAccount = findViewById(R.id.tvNewAccount);
    }

    boolean validateInput() {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        return true;
    }

    void Notify(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void sendNotification(String title, String message, int userId) {
        NotificationManager manager = NotificationConfig.getNotificationManger(this);
        Notification.Builder builder = NotificationConfig.getBuilder(this, title, message);
        Notification notification = builder.build();
        if (manager != null) {
            manager.notify(userId, notification);
        }
    }

    //Event Handler
    void buttonLogin_Click() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String phone = etPhone.getText().toString();
                    String password = etPassword.getText().toString();
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void run() {
                            User user = db.userDAO().getUserByPhoneAndPassword(phone, password);
                            if (user != null) {
                                if (user.getRole().equals(Constants.USER)) {
                                    //Notify user if cart is not empty
                                    Order cart = db.orderDAO().getCartByUserId(user.getId());
                                    if (cart != null) {
                                        List<OrderDetail> details = db.orderDetailDAO().getDetailListByOrderId(cart.getId());
                                        if (details != null && details.size() > 0) {
                                            sendNotification(Constants.APP_NAME, String.format(Constants.NOTIFICATION_AFTER_LOGIN, details.size()), user.getId());
                                        }
                                    }
                                    //Redirect to Book Activity
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences shared = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = shared.edit();
                                            editor.putInt(Constants.USER_ID, user.getId());
                                            editor.apply();
                                            intent = new Intent(LoginActivity.this, BookActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                else if (user.getRole().equals(Constants.ADMIN)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            } else {
                                Notify(Constants.LOGIN_FAILED_MESSAGE);
                            }
                        }
                    });
                }
            }
        });
    }

    void NewAccount_Click() {
        tvNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, CreateNewActivity.class);
                startActivity(intent);
            }
        });
    }
}