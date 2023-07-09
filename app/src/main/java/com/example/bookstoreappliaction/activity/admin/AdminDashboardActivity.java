package com.example.bookstoreappliaction.activity.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.login.LoginActivity;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnProducts, btnUsers, btnLogOut;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        
        btnProducts = findViewById(R.id.btnProducts);
        btnUsers = findViewById(R.id.btnUsers);
        btnLogOut = findViewById(R.id.btnLogout);
        
        btnProducts.setOnClickListener(this);
        btnUsers.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnProducts){
            intent = new Intent(AdminDashboardActivity.this, AdminBookListActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.btnUsers){
            intent = new Intent(AdminDashboardActivity.this, AdminUserListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnLogout) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}