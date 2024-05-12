package com.example.myapplication.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context mContext;
    private ArrayList<ChatMessage> mMessages;

    public ChatAdapter(Context context, ArrayList<ChatMessage> messages) {
        mContext = context;
        mMessages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = mMessages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView;
        LinearLayout rightChatView;
        TextView leftTextView;
        TextView rightTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
        }

        public void bind(ChatMessage message) {
            if (message.isSentByMe()) {
                leftChatView.setVisibility(View.GONE);
                rightChatView.setVisibility(View.VISIBLE);
                rightTextView.setText(message.getMessage());
            } else {
                rightChatView.setVisibility(View.GONE);
                leftChatView.setVisibility(View.VISIBLE);
                leftTextView.setText(message.getMessage());
            }
        }
    }
}
