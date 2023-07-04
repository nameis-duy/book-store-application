package com.example.bookstoreappliaction.activity.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookActivity;
import com.example.bookstoreappliaction.activity.orders.OrdersActivity;
import com.example.bookstoreappliaction.activity.users.UserProfile;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    final int REQUEST_PERMISSION_REQUEST_CODE = 111;
    MapView map;
    GeoPoint startPoint;
    Marker marker;
    Intent intent;
    //
    ImageButton btnHome, btnProfile, btnLocation, btnOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        //
        setContentView(R.layout.activity_map);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initView();
        initStartPoint();
        initMarker();
        //
        btnHome_OnClick();
        btnProfile_OnClick();
        btnOrders_OnClick();
    }

    void initView() {
        map = findViewById(R.id.map);
        //
        btnHome = findViewById(R.id.btnHome);
        btnProfile = findViewById(R.id.btnProfile);
        btnLocation = findViewById(R.id.btnLocation);
        btnOrders = findViewById(R.id.btnOrders);
        //
        map.setTileSource(TileSourceFactory.MAPNIK);

        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
    }

    void initStartPoint() {
        IMapController mapController = map.getController();
        mapController.setZoom(18);
        startPoint = new GeoPoint(10.841359410985072, 106.81035506407625);
        mapController.setCenter(startPoint);
    }

    void initMarker() {
        marker = new Marker(map);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(marker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSION_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSION_REQUEST_CODE);
        }
    }
    //Event Handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent(MapActivity.this, BookActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    void btnHome_OnClick() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MapActivity.this, BookActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void btnProfile_OnClick() {
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MapActivity.this, UserProfile.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void btnOrders_OnClick() {
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MapActivity.this, OrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}