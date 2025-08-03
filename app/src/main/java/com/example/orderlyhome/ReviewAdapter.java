package com.example.orderlyhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private final List<reviews> reviewsList = new ArrayList<>();
    private final ListenerRegistration listenerReg;

    public ReviewAdapter(String organizerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Reviews")
                .whereEqualTo("organizerId", organizerId);


        listenerReg = query.addSnapshotListener((snap, err) -> {
            if (err != null || snap == null) return;

            for (DocumentChange dc : snap.getDocumentChanges()) {
                QueryDocumentSnapshot doc = dc.getDocument();
                reviews item = mapDocToReview(doc, organizerId);
                int oldIndex = dc.getOldIndex();
                int newIndex = dc.getNewIndex();

                switch (dc.getType()) {
                    case ADDED:
                        // only insert if we haven't already manually added this review
                        boolean seen = false;
                        for (reviews r : reviewsList) {
                            if (r.getID().equals(item.getID())) {
                                seen = true;
                                break;
                            }
                        }
                        if (!seen) {
                            reviewsList.add(dc.getNewIndex(), item);
                            notifyItemInserted(dc.getNewIndex());
                        }
                        break;

                    case MODIFIED:
                        reviewsList.set(oldIndex, item);
                        notifyItemChanged(oldIndex);
                        break;
                    case REMOVED:
                        for (int i = 0; i < reviewsList.size(); i++) {
                            if (reviewsList.get(i).getID().equals(item.getID())) {
                                reviewsList.remove(i);
                                notifyItemRemoved(i);
                                break;
                            }
                        }
                        break;

                }
            }
        });
    }

    private reviews mapDocToReview(QueryDocumentSnapshot doc, String organizerId) {
        String id     = doc.getString("ID");
        String stars  = doc.getString("Reviewstars");
        String name   = doc.getString("Reviewer");
        String text   = doc.getString("Review");
        reviews r = new reviews(stars, name, text, id);
        r.setOrganizerId(organizerId);
        return r;
    }

    @NonNull @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        reviews r = reviewsList.get(position);
        holder.reviewer1    .setText(r.getReviewer());
        holder.reviewstars1 .setText(r.getReviewstars1());
        holder.review1      .setText(r.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    /**
     * Bring back your old helper so Screen2_MoreInfo can call it!
     */
    public void addReviews(reviews r) {
        reviewsList.add(r);
        notifyItemInserted(reviewsList.size() - 1);
    }

    /** Call in Activity.onDestroy() to remove the Firestore listener */
    public void cleanup() {
        listenerReg.remove();
    }
}