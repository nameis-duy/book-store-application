package com.example.bookstoreappliaction.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.User;

public class CreateNewActivity extends AppCompatActivity {

    Button btnSignUp;
    EditText etName, etPhone, etPassword, etConfirmPassword;
    TextView tvLogin;
    //
    BookStoreDb db;
    Intent intent;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);

        initView();
        initDb();
        //
        BackToLogin_Click();
        SignUp_Click();
    }

    void initView() {
        btnSignUp = findViewById(R.id.btnSignUp);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        tvLogin = findViewById(R.id.tvLogin);
    }

    void initDb() {
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
    }

    boolean validateInput() {
        String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etName.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError(Constants.REQUIRE_MESSAGE);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError(Constants.CONFIRM_PASSWORD_ERROR);
            return false;
        }

        return true;
    }

    //Event handler
    void BackToLogin_Click() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                finish();
            }
        });
    }

    void SignUp_Click() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    String name = etName.getText().toString();
                    String phone = etPhone.getText().toString();
                    String password = etPassword.getText().toString();
                    //
                    AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            User user = db.userDAO().getUserByPhone(phone);
                            if (user == null) {
                                User createUser = new User(name, phone, password, Constants.ADMIN);
                                db.userDAO().insert(createUser);
                                intent = new Intent();
                                finish();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CreateNewActivity.this, "Register succeed.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        etPhone.setError(Constants.PHONE_REGISTER_ERROR);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}