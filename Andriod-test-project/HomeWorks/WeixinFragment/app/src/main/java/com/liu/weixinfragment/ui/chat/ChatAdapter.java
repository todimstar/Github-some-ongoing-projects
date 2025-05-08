package com.liu.weixinfragment.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liu.weixinfragment.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<ChatItem> chatList;

    public ChatAdapter(Context context, List<ChatItem> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem item = chatList.get(position);
        holder.avatar.setImageResource(item.getAvatarResId());
        holder.name.setText(item.getName());
        holder.lastMessage.setText(item.getLastMessage());
        holder.timestamp.setText(item.getTimestamp());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "打开聊天: " + item.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return chatList == null ? 0 : chatList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        TextView lastMessage;
        TextView timestamp;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.chat_item_avatar);
            name = itemView.findViewById(R.id.chat_item_name);
            lastMessage = itemView.findViewById(R.id.chat_item_last_message);
            timestamp = itemView.findViewById(R.id.chat_item_timestamp);
        }
    }
}