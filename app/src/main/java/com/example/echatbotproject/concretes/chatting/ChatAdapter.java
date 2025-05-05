package com.example.echatbotproject.concretes.chatting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.echatbotproject.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false); // Create this layout file
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.messageTextView.setText(message.getText());
        // You can add logic here to style user vs. bot messages differently
        if (message.isUser()) {
            holder.messageTextView.setBackgroundResource(android.R.color.holo_blue_light);
            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            holder.messageTextView.setBackgroundResource(android.R.color.holo_green_light);
            holder.messageTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textViewMessage); // Create this TextView in item_chat_message.xml
        }
    }
}