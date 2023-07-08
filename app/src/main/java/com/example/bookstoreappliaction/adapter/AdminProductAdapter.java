package com.example.bookstoreappliaction.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Context context;

    public AdminProductAdapter(List<Book> bookList) { this.bookList = bookList; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice;
        ImageView imgProduct;
        Button btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNames);
            tvPrice = itemView.findViewById(R.id.tvPrices);
            imgProduct = itemView.findViewById(R.id.imgBooks);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.admin_book_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.btnUpdate.setOnClickListener(v -> {
            int position = viewHolder.getAdapterPosition();
            int bookId =(int) bookList.get(position).getId();
            Intent intent = new Intent(context, CreateProductActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("UpdateId", bookId);
            context.startActivity(intent);
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.tvName.setText(book.getTitle());
        holder.tvPrice.setText("$" + book.getPrice());
        Picasso.get()
                .load(book.getImageUrl())
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        if (bookList == null) return 0;
        return bookList.size();
    }

}
