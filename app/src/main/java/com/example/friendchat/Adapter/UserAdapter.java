package com.example.friendchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.friendchat.MessageActivity;
import com.example.friendchat.Model.Users;
import com.example.friendchat.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers;
    private boolean isChat;

    //Constractor

    public UserAdapter(Context context, List<Users> mUsers, boolean isChat) {
        this.context = context;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());
        if(users.getImageURL().equals("default")){
            holder.imageView.setImageResource(R.drawable.profile);
        }else{
            // Adding Glide Library
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.imageView);
        }

        //status check
        if (isChat){
            if (users.getStatus().equals("Online")){
                holder.imageViewON.setVisibility(View.VISIBLE);
                holder.getImageViewOFF.setVisibility(View.GONE);
            }else {
                holder.imageViewON.setVisibility(View.GONE);
                holder.getImageViewOFF.setVisibility(View.VISIBLE);
            }
        }else{
            holder.imageViewON.setVisibility(View.GONE);
            holder.getImageViewOFF.setVisibility(View.GONE);
        }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, MessageActivity.class);
               intent.putExtra("userid", users.getId());
               context.startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView imageView;
        public ImageView imageViewON;
        public ImageView getImageViewOFF;

        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            imageViewON = itemView.findViewById(R.id.statusimageON);
            getImageViewOFF = itemView.findViewById(R.id.statusimageOFF);
        }
    }
}
