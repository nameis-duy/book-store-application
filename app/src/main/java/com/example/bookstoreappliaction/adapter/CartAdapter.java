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
import com.example.bookstoreappliaction.activity.cart.CartActivity;
import com.example.bookstoreappliaction.database.BookStoreDb;
import com.example.bookstoreappliaction.executors.AppExecutors;
import com.example.bookstoreappliaction.models.Book;
import com.example.bookstoreappliaction.models.Order;
import com.example.bookstoreappliaction.models.OrderDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<OrderDetail> carts;

    public CartAdapter(List<OrderDetail> orders) {
        this.carts = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.cart_items, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetail cart = carts.get(position);
        //
        BookStoreDb db = ((CartActivity) context).getDB();
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Book book = db.bookDAO().getBookById(cart.getBookId());
                ((CartActivity) context).runOnUiThread(new Runnable() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void run() {
                        holder.tvBookTitle.setText(book.getTitle());
                        holder.tvPrice.setText(String.format("$%s", cart.getUnitPrice()));
                        holder.etQuantity.setText(String.format("%d", cart.getQuantity()));
                        Picasso.get().load(book.getImageUrl()).into(holder.imgBook);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (carts != null) return carts.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvPrice;
        ImageView imgBook;
        Button btnDecrease, btnIncrease;
        ImageButton ibRemove;
        EditText etQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            //
            imgBook = itemView.findViewById(R.id.imgBook);
            //
            btnDecrease = itemView.findViewById(R.id.btnDecrement);
            btnIncrease = itemView.findViewById(R.id.btnIncrement);
            //
            ibRemove = itemView.findViewById(R.id.btnRemoveFromCart);
            //
            etQuantity = itemView.findViewById(R.id.etQuantity);
        }
    }
}
