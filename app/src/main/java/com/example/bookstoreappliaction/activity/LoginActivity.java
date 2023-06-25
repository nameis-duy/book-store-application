package com.example.bookstoreappliaction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    //
    BookStoreDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        initViews();
        initDb();
        //
        buttonLogin_Click();
    }

    private boolean IsExistDB(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    void initDb() {
        if (IsExistDB(getApplicationContext(), Constants.DB_NAME)) {
            db = Room.databaseBuilder(getApplicationContext(),
                    BookStoreDb.class, Constants.DB_NAME).build();
        }
    }

    void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    void buttonLogin_Click() {
        String phone = etPhone.getText().toString();
        String password = etPhone.getText().toString();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<User> users = db.userDAO().getAll();
                        Log.d("msg", users.size() + "");
                        User user = db.userDAO().getUserByPhoneAndPassword(phone, password);
                        if (user != null) {
                            Log.d("msg", "Succeed");
                        }
                        else {
                            Log.d("msg", "Failed");
                        }
                    }
                });
            }
        });
    }
}