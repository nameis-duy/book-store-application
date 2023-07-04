package com.example.bookstoreappliaction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookDetailActivity;
import com.example.bookstoreappliaction.activity.orders.OrderDetailsActivity;
import com.example.bookstoreappliaction.activity.orders.OrdersActivity;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.order_items, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvId.setText(String.format("Order ID: #%d", order.getId()));
        holder.tvDate.setText("Order Date: " + formatDate(order.getOrderDate()));
        holder.tvTotal.setText(String.format("Total: $%.2f", order.getTotal()));

        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra(Constants.ORDER_ID, order.getId());
                intent.putExtra(Constants.ORDER_TOTAL, order.getTotal());
                context.startActivity(intent);
            }
        });
    }

    String formatDate(Date date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatDisplay = new SimpleDateFormat("dd-MM-yyyy");
        return formatDisplay.format(date);
    }

    @Override
    public int getItemCount() {
        if (orders == null) return 0;
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvDate, tvTotal;
        Button btnDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvId);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnDetails = itemView.findViewById(R.id.btnDetail);
        }
    }
}
