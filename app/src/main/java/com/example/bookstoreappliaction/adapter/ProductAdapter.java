package com.example.bookstoreappliaction.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.book.BookDetailActivity;
import com.example.bookstoreappliaction.constants.Constants;
import com.example.bookstoreappliaction.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context context;
    List<Book> books;

    public ProductAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.product_items, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        holder.tvName.setText(book.getTitle());
        holder.tvPrice.setText("$" + book.getPrice());
        Picasso.get()
                .load(book.getImageUrl())
                .into(holder.imgProduct);

        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetail(book);
            }
        });

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetail(book);
            }
        });
    }

    void ProductDetail(Book book) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(Constants.BOOK_DETAIL_ID, book.getId());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (books == null) return 0;
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice;
        ImageView imgProduct;
        Button btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgBook);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
