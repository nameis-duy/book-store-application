package com.example.bookstoreappliaction.activity.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.adapter.UserAdapter;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.User;

import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {
    RecyclerView rvUserList;
    List<User> users;
    BookStoreDb db;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

        rvUserList = findViewById(R.id.activity_adminuserlist_rv_userList);
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
}