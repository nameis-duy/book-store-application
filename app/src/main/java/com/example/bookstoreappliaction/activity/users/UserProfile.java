package com.example.bookstoreappliaction.activity.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.activity.login.LoginActivity;
import com.example.bookstoreappliaction.activity.map.MapActivity;
import com.example.bookstoreappliaction.activity.orders.OrdersActivity;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.User;
import com.example.bookstoreappliaction.utils.StringUtils;

import java.util.List;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    TextView tvFacebook, tvOrders, tvPassword;
    EditText etName, etPhone, etCurrentPassword, etNewPassword, etConfirmPassword;
    ImageButton btnHome, btnLocation, btnOrders, btnEditName;
    Button btnSave;
    //
    Intent intent;
    User loginUser;
    List<Order> orders;
    //
    BookStoreDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initDb();
        initView();
        initEditTextEnabled();
        initUser();
        //
        etPassword_OnClick();
        btnEditName_OnClick();
        btnSave_OnClick();
        btnHome_OnClick();
        btnLocation_OnClick();
        btnOrders_OnClick();
    }

    void initUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.LOGIN, MODE_PRIVATE);
        int userId = sharedPreferences.getInt(Constants.USER_ID, -1);
        if (userId == -1) {
            intent = new Intent(UserProfile.this, LoginActivity.class);
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
                                intent = new Intent(UserProfile.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        orders = db.orderDAO().getOrdersByUserId(loginUser.getId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initViewValue();
                            }
                        });
                    }
                }
            });
        }
    }

    void initView() {
        tvFacebook = findViewById(R.id.tvFacebook);
        tvOrders = findViewById(R.id.tvOrders);
        //
        etName = findViewById(R.id.tvName);
        tvPassword = findViewById(R.id.tvPassword);
        etPhone = findViewById(R.id.tvPhone);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        //
        btnHome = findViewById(R.id.btnHome);
        btnLocation = findViewById(R.id.btnLocation);
        btnOrders = findViewById(R.id.btnOrders);
        btnEditName = findViewById(R.id.btnEditName);
        //
        btnSave = findViewById(R.id.btnSave);
        btnSave.setVisibility(View.INVISIBLE);
    }

    void initEditTextEnabled() {
        //
        etName.setFocusable(false);
        etName.setEnabled(false);
        //
        etPhone.setFocusable(false);
        etPhone.setEnabled(false);
        //
        etCurrentPassword.setVisibility(View.INVISIBLE);
        etNewPassword.setVisibility(View.INVISIBLE);
        etConfirmPassword.setVisibility(View.INVISIBLE);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    void initViewValue() {
        //init Value
        tvOrders.setText(String.format("%d", orders != null ? orders.size() : 0));
        //
        etName.setText(loginUser.getName());
        etPhone.setText(StringUtils.FormatPhone(loginUser.getPhone()));
        tvPassword.setText(loginUser.getPassword());
        //
        tvFacebook.setText("facebook.com/" + loginUser.getName());
    }

    void initDb() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    @SuppressLint("SetTextI18n")
    void nameUpdateConfig() {
        etName.setBackgroundColor(getResources().getColor(R.color.grey));
        etName.setTextColor(Color.WHITE);
        tvFacebook.setText("facebook.com/" + StringUtils.removeEmptySpace(loginUser.getName()));
        initEditTextEnabled();
    }

    void passwordUpdateConfig() {
        etCurrentPassword.setVisibility(View.INVISIBLE);
        etNewPassword.setVisibility(View.INVISIBLE);
        etConfirmPassword.setVisibility(View.INVISIBLE);
    }

    boolean isUpdateName() {
        return etCurrentPassword.getVisibility() == View.INVISIBLE;
    }

    boolean validateNewPassword(String curPassword, String newPassword, String confirmPassword) {
        if (TextUtils.isEmpty(curPassword)) {
            etCurrentPassword.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }
        if (TextUtils.isEmpty(newPassword)) {
            etNewPassword.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }
        //
        if (!curPassword.equals(loginUser.getPassword())) {
            etCurrentPassword.setError(Constants.USER_INCORRECT_PASSWORD_ERROR);
            return false;
        }
        //
        if (curPassword.equals(newPassword)) {
            etNewPassword.setError(Constants.USER_NEW_PASSWORD_EQUALS_CURRENT_PASSWORD_ERROR);
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError(Constants.CONFIRM_PASSWORD_ERROR);
            return false;
        }

        return true;
    }

    //Event Handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent(UserProfile.this, BookActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void etPassword_OnClick() {
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCurrentPassword.getVisibility() == View.INVISIBLE) {
                    etCurrentPassword.setVisibility(View.VISIBLE);
                    etNewPassword.setVisibility(View.VISIBLE);
                    etConfirmPassword.setVisibility(View.VISIBLE);
                    btnSave.setVisibility(View.VISIBLE);
                } else {
                    passwordUpdateConfig();
                    btnSave.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    void btnEditName_OnClick() {
        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSave.getVisibility() == View.INVISIBLE) {
                    etName.setFocusable(true);
                    etName.setEnabled(true);
                    etName.setBackgroundColor(Color.WHITE);
                    etName.setTextColor(Color.GRAY);
                    btnSave.setVisibility(View.VISIBLE);
                } else {
                    nameUpdateConfig();
                    btnSave.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    void btnSave_OnClick() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdateName()) {
                    String newName = etName.getText().toString();
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            loginUser.setName(newName);
                            db.userDAO().update(loginUser);
                        }
                    });
                    nameUpdateConfig();
                    Toast.makeText(UserProfile.this, Constants.USER_UPDATE_SUCCEED, Toast.LENGTH_SHORT).show();
                    btnSave.setVisibility(View.INVISIBLE);
                } else {
                    String curPassword = etCurrentPassword.getText().toString();
                    String newPassword = etNewPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();
                    if (validateNewPassword(curPassword, newPassword, confirmPassword)) {
                        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                loginUser.setPassword(newPassword);
                                db.userDAO().update(loginUser);
                            }
                        });
                        passwordUpdateConfig();
                        Toast.makeText(UserProfile.this, Constants.USER_UPDATE_SUCCEED, Toast.LENGTH_SHORT).show();
                        btnSave.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    void btnHome_OnClick() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfile.this, BookActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void btnLocation_OnClick() {
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfile.this, MapActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void btnOrders_OnClick() {
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(UserProfile.this, OrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}