package com.example.orderlyhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    private List<reviews> reviewsList; // List to hold the reviews data

    // Constructor to initialize the adapter with the list of reviews
    public ReviewAdapter(List<reviews> reviewsList) {
        this.reviewsList = reviewsList;
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
        holder.n_logo.setImageResource(review.getLogo());        // Set the reviewer's logo image
    }

    // This method returns the number of items in the reviews list
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
