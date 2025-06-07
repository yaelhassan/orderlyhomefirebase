package com.example.orderlyhome;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// The ReviewViewHolder class is used to hold references to the views in a single item in the RecyclerView for reviews
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    // Declare the views that will be populated for each review
//    public ImageView n_logo;          // ImageView for the reviewer's logo or avatar
    public TextView reviewer1;        // TextView for the reviewer's name
    public TextView review1;          // TextView for the review content
    public TextView reviewstars1;     // TextView for the star rating of the review

    // Constructor to initialize the views
    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);  // Call to the parent class constructor (RecyclerView.ViewHolder)

        // Initialize each view using findViewById with the respective IDs from the XML layout
//        this.n_logo = itemView.findViewById(R.id.n_logo);
        this.reviewer1 = itemView.findViewById(R.id.reviewer1);
        this.review1 = itemView.findViewById(R.id.review1);
        this.reviewstars1 = itemView.findViewById(R.id.reviewstars1);
    }
}
