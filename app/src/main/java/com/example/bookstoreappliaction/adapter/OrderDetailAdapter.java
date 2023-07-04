package com.example.bookstoreappliaction.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.orders.OrderDetailsActivity;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    Context context;
    List<OrderDetail> details;

    public OrderDetailAdapter(List<OrderDetail> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.order_details_items, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail detail = details.get(position);

        BookStoreDb db = ((OrderDetailsActivity) context).getDb();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Book book = db.bookDAO().getBookById(detail.getBookId());
                if (book != null) {
                    ((OrderDetailsActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get().load(book.getImageUrl()).into(holder.imgBook);
                            holder.tvBookTitle.setText(book.getTitle());
                            holder.tvPrice.setText(String.format("$%.2f", detail.getUnitPrice()));
                            holder.tvQuantity.setText(String.format("Qty: %d", detail.getQuantity()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (details == null) return 0;
        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBookTitle, tvPrice, tvQuantity;
        ImageView imgBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            //
            imgBook = itemView.findViewById(R.id.imgBook);
        }
    }
}
