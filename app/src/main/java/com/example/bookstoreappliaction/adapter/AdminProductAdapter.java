package com.example.bookstoreappliaction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.activity.admin.CreateProductActivity;
import com.example.bookstoreappliaction.models.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    private List<Book> bookList;
    Context context;

    public AdminProductAdapter(List<Book> bookList) { this.bookList = bookList; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice;
        ImageView imgProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNames);
            tvPrice = itemView.findViewById(R.id.tvPrices);
            imgProduct = itemView.findViewById(R.id.imgBooks);

            imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int elementId = bookList.get(getAdapterPosition()).getId();
                    int gId = bookList.get(getAdapterPosition()).getGenreId();
                    Intent i = new Intent(context, CreateProductActivity.class);
                    i.putExtra("ProductId", elementId);
                    i.putExtra("genreId", gId);
                    context.startActivity(i);
                }
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_book_items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvName.setText(book.getTitle());
        holder.tvPrice.setText("$" + book.getPrice());
        Picasso.get()
                .load(book.getImageUrl())
                .into(holder.imgProduct);

//        holder.imgProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProductDetail(book);
//            }
//        });
//
//        holder.tvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProductDetail(book);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (bookList == null) return 0;
        return bookList.size();
    }

    public void setTasks(List<Book> mbookList) {
        bookList = mbookList;
        notifyDataSetChanged();
    }

    public List<Book> getTasks() {
        return bookList;
    }

}
