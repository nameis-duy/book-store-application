package com.example.bookstoreappliaction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.dao.UserDAO;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.User;

import java.io.File;
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

    //Event Handler
    void buttonLogin_Click() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String phone = etPhone.getText().toString();
                    String password = etPassword.getText().toString();
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            User user = db.userDAO().getUserByPhoneAndPassword(phone, password);
                            if (user != null) {
                                Log.d("msg", "Succeed");
                            } else {
                                Log.d("msg", "Failed");
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