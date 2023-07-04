package com.example.bookstoreappliaction.activity.payment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.constants.ConfigurationConstants;
import com.example.bookstoreappliaction.constants.Constants;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {
    final String TAG = "Stripe-msg";
    //
    float total;
    Intent intent;
    //
    String CustomerId, EphericalKey, ClientSecret;
    PaymentSheet paymentSheet;
    //
    ImageButton btnPayment;
    ProgressBar pbWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //
        initView();
        //
        total = getIntent().getFloatExtra(Constants.ORDER_TOTAL, -1);
        if (total == -1) {
            intent = new Intent();
            intent.putExtra(Constants.PAYMENT_MESSAGE, Constants.PAYMENT_FAILED);
            setResult(99, intent);
            finish();
        } else {
            PaymentConfiguration.init(this, ConfigurationConstants.STRIPE_PUBLIC_KEY);

            paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
                onPaymentResult(paymentSheetResult);
            });

            initKey();

            btnPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentFlow();
                }
            });
        }
    }

    void initView() {
        btnPayment = findViewById(R.id.btnPayment);
        pbWaiting = findViewById(R.id.pbWaiting);
        //
        Handler handler = new Handler();
        btnPayment.setVisibility(View.INVISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                btnPayment.setVisibility(View.VISIBLE);
                pbWaiting.setVisibility(View.INVISIBLE);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            intent = new Intent();
            intent.putExtra(Constants.PAYMENT_MESSAGE, Constants.PAYMENT_SUCCEED);
            setResult(99, intent);
            finish();
        } else {
            intent = new Intent();
            intent.putExtra(Constants.PAYMENT_MESSAGE, Constants.PAYMENT_FAILED);
            setResult(99, intent);
            finish();
        }
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Test Stripe", new PaymentSheet.CustomerConfiguration(
                CustomerId,
                EphericalKey
        )));
    }

    void initKey() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            CustomerId = object.getString("id");

                            getEmphericalKey();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + ConfigurationConstants.STRIPE_SECRET_KEY);
                return header;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getEmphericalKey() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");

                            getClientSecret();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + ConfigurationConstants.STRIPE_SECRET_KEY);
                header.put("Stripe-Version", "2022-11-15");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void getClientSecret() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + ConfigurationConstants.STRIPE_SECRET_KEY);
                return header;
            }

            @SuppressLint("DefaultLocale")
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", String.format("%d00", (int) total));
                params.put("currency", "USD");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    //Event Handler
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intent = new Intent();
            intent.putExtra(Constants.PAYMENT_MESSAGE, Constants.PAYMENT_FAILED);
            setResult(99, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}