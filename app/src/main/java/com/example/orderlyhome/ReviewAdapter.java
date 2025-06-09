package com.example.orderlyhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;
import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private List<reviews> reviewsList; // List to hold the reviews data
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Constructor to initialize the adapter with the list of reviews
    public ReviewAdapter() {
        this.reviewsList = new ArrayList<>();
        db.collection("Reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            public void onComplete(@NonNull Task <QuerySnapshot> task){
                if (task.isSuccessful()) {
                    reviewsList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        Long longLogo = doc.getLong("Logo");
//
//                        int logoRes = longLogo.intValue();
                        String stars = doc.getString("Reviewstars");
                        String reviewerName = doc.getString("Reviewer");
                        String reviewText = doc.getString("Review");
                        String reviewId  = doc.getString("ID");

                        reviews r = new reviews(stars, reviewerName, reviewText, reviewId);
                        reviewsList.add(r);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        db.collection("Reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        reviewsList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            //Long longLogo = doc.getLong("Logo");
                           // int logoRes = (longLogo != null) ? longLogo.intValue() : 0;
                            String stars        = doc.getString("Reviewstars");
                            String reviewerName = doc.getString("Reviewer");
                            String reviewText   = doc.getString("Review");
                            String reviewId     = doc.getString("ID");

                            reviews r = new reviews(stars, reviewerName, reviewText, reviewId);
                            reviewsList.add(r);
                        }
                        notifyDataSetChanged();
                    }
                });
    }

    // This method is called when a new ViewHolder is needed
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view for a single review item from the layout XML (R.layout.reviews)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent, false);

        // Create and return a new instance of ReviewViewHolder with the inflated view
        return new ReviewViewHolder(view);
    }

    // This method binds data from the reviews list to the views in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Get the review data for the current position
        reviews review = reviewsList.get(position);

        // Set the text and image for the views in the ViewHolder
        holder.reviewer1.setText(review.getReviewer());          // Set the reviewer's name
        holder.review1.setText(review.getReview());              // Set the review content
        holder.reviewstars1.setText(review.getReviewstars1());  // Set the star rating text
//        holder.n_logo.setImageResource(review.getLogo());        // Set the reviewer's logo image
    }

    // This method returns the number of items in the reviews list
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}