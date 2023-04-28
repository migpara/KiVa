package com.example.kiva;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MY_MESSAGE = 1;
    private static final int VIEW_TYPE_OTHERS_MESSAGE = 2;

    private List<Message> messages;
    private String currentUser;

    public BlogAdapter(List<Message> messages, String currentUser) {
        this.messages = messages;
        this.currentUser = currentUser;
    }
    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getUserId().equals(currentUser)) {
            return VIEW_TYPE_MY_MESSAGE;
        } else {
            return VIEW_TYPE_OTHERS_MESSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);
            return new MyMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vistamensaje, parent, false);
            return new OthersMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        if (holder.getItemViewType() == VIEW_TYPE_MY_MESSAGE) {
            ((MyMessageViewHolder) holder).content.setText(message.getContent());
            ((MyMessageViewHolder) holder).timestamp.setText(dateFormat.format(message.getTimestamp()));
        } else {
            ((OthersMessageViewHolder) holder).content.setText(message.getContent());
            ((OthersMessageViewHolder) holder).timestamp.setText(dateFormat.format(message.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MyMessageViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView timestamp;

        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.my_text_message);
            timestamp = itemView.findViewById(R.id.my_text_message_time);
        }
    }

    public static class OthersMessageViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView timestamp;

        public OthersMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.text_message);
            timestamp = itemView.findViewById(R.id.text_message_time);
        }
    }
}


