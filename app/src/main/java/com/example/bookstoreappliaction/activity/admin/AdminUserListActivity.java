package com.example.bookstoreappliaction.activity.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.login.LoginActivity;
import com.example.bookstoreappliaction.adapter.UserAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.User;

import java.util.List;

public class AdminUserListActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rvUserList;
    List<User> users;
    ImageButton btnDashboard, btnProducts, btnLogOut;
    Intent intent;
    BookStoreDb db;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

        rvUserList = findViewById(R.id.activity_adminuserlist_rv_userList);

        btnDashboard = findViewById(R.id.btnDashboard);
        btnProducts = findViewById(R.id.btnProducts);
        btnLogOut = findViewById(R.id.btnLogout);

        btnDashboard.setOnClickListener(this);
        btnProducts.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        db = Room.databaseBuilder(getApplicationContext(),
                BookStoreDb.class, Constants.DB_NAME).build();
        LoadList();
    }

    void LoadList(){
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            users = db.userDAO().getAll();
            if(users!=null){
                runOnUiThread(() -> {
                    adapter = new UserAdapter(users);
                    rvUserList.setAdapter(adapter);
                    rvUserList.setLayoutManager(new LinearLayoutManager(AdminUserListActivity.this));
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnDashboard){
            intent = new Intent(AdminUserListActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btnProducts){
            intent = new Intent(AdminUserListActivity.this, AdminBookListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnLogout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        intent = new Intent(AdminUserListActivity.this, LoginActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}