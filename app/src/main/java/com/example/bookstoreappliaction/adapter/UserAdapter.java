package com.example.bookstoreappliaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstoreappliaction.R;
import com.example.bookstoreappliaction.models.User;

import java.util.List;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.admin_user_items, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvUserId.setText(String.valueOf(user.getId()));
        holder.tvUserName.setText(user.getName());
        holder.tvUserPhone.setText(user.getPhone());
        holder.tvUserPassword.setText(user.getPassword());
        holder.tvUserRole.setText(user.getRole());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserId, tvUserName, tvUserPhone, tvUserPassword, tvUserRole;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserId = itemView.findViewById(R.id.row_adminuserlist_tv_userId);
            tvUserName = itemView.findViewById(R.id.row_adminuserlist_tv_userName);
            tvUserPhone = itemView.findViewById(R.id.row_adminuserlist_tv_userPhone);
            tvUserPassword = itemView.findViewById(R.id.row_adminuserlist_tv_userPassword);
            tvUserRole = itemView.findViewById(R.id.row_adminuserlist_tv_userRole);
        }
    }
}
