package com.example.orderlyhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final List<Message> messageList = new ArrayList<>();
    private final ListenerRegistration listenerReg;

    public MessageAdapter(String organizerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Only filter by organizerId—no orderBy(), so no index needed
        listenerReg = db.collection("Messages")
                .whereEqualTo("organizerId", organizerId)
                .addSnapshotListener((snap, err) -> {
                    if (err != null || snap == null) return;

                    // 1) Rebuild entire list from the up‑to‑date snapshot
                    messageList.clear();
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        Map<String,Object> data = doc.getData();
                        String text     = (String) data.get("text");
                        String imageUri = (String) data.get("imageUri");
                        Boolean sentBy  = (Boolean) data.get("sentByUser");
                        Long ts         = (Long)    data.get("timestamp");

                        Message m = (imageUri != null)
                                ? new Message(imageUri, sentBy != null && sentBy, ts != null ? ts : 0L, true)
                                : new Message(text,    sentBy != null && sentBy, ts != null ? ts : 0L);

                        messageList.add(m);
                    }

                    // 2) Sort chronologically by timestamp
                    Collections.sort(
                            messageList,
                            Comparator.comparingLong(Message::getTimestamp)
                    );

                    // 3) Refresh the RecyclerView
                    notifyDataSetChanged();
                });
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message m = messageList.get(position);
        if (m.hasImage()) {
            holder.textMessage.setVisibility(View.GONE);
            holder.imageMessage.setVisibility(View.VISIBLE);
            Glide.with(holder.imageMessage.getContext())
                    .load(m.getImageUri())
                    .into(holder.imageMessage);
        } else {
            holder.imageMessage.setVisibility(View.GONE);
            holder.textMessage.setVisibility(View.VISIBLE);
            holder.textMessage.setText(m.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    /** Instant local feedback when sending */
    public void addMessage(Message m) {
        messageList.add(m);
        notifyItemInserted(messageList.size() - 1);
    }

    /** Detach the Firestore listener to avoid leaks */
    public void cleanup() {
        listenerReg.remove();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageMessage;
        public final TextView  textMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageMessage = itemView.findViewById(R.id.image_message);
            textMessage  = itemView.findViewById(R.id.text_message);
        }
    }
}
