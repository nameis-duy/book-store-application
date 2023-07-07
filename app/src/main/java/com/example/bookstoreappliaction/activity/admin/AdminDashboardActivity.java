package com.example.bookstoreappliaction.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bookstoreappliaction.R;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnProducts, btnUsers;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnProducts = findViewById(R.id.activity_adminpanel_btn_products);
        btnUsers = findViewById(R.id.activity_adminpanel_btn_users);

        btnProducts.setOnClickListener(this);
        btnUsers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.activity_adminpanel_btn_products){
            intent = new Intent(AdminDashboardActivity.this, AdminBookListActivity.class);
        }else if(v.getId() == R.id.activity_adminpanel_btn_users){
            intent = new Intent(AdminDashboardActivity.this, AdminUserListActivity.class);
        }
        startActivity(intent);
    }
}