package com.example.orderlyhome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Screen2_MoreInfo extends AppCompatActivity {

    // onCreate method - where the activity starts executing.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen2_more_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the 'Organizer' object passed from the previous activity via Intent
        // This will be used to populate the details on the new screen
        Organizer selectedOrganizer = (Organizer) getIntent().getSerializableExtra("selectedOrganizer");

        // Finding the views by their IDs from the XML layout
        TextView nameText = findViewById(R.id.name_a);  // Organizer's name
        TextView placeText = findViewById(R.id.place_a); // Organizer's location
        TextView feeText = findViewById(R.id.fee_a);     // Organizer's hourly fee
        TextView ageText = findViewById(R.id.age_a);     // Organizer's age
        TextView detailsText = findViewById(R.id.details_a); // Description of the organizer
        ImageView displayPic = findViewById(R.id.displaypicture_a); // Display picture of the organizer
        ImageView portfolioPic1 = findViewById(R.id.portfoliopic1); // First portfolio picture
        ImageView portfolioPic2 = findViewById(R.id.portfoliopic2); // Second portfolio picture

        // Setting the views with data retrieved from the selectedOrganizer object
        nameText.setText(selectedOrganizer.getName()); // Display name
        placeText.setText(selectedOrganizer.getLocation()); // Display location
        feeText.setText(selectedOrganizer.getFee()); // Display fee
        ageText.setText("Age: " + selectedOrganizer.getAge()); // Display age with a label
        displayPic.setImageResource(selectedOrganizer.getImageResId()); // Display profile image
        detailsText.setText(selectedOrganizer.getDescription()); // Display detailed description
        portfolioPic1.setImageResource(selectedOrganizer.getPortfolioPic1ResId()); // Display first portfolio image
        portfolioPic2.setImageResource(selectedOrganizer.getPortfolioPic2ResId()); // Display second portfolio image

        // Setting up a RecyclerView to display reviews related to the selected organizer
        RecyclerView reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerView.setHasFixedSize(false); // Allow dynamic sizing of RecyclerView items

        // Using a GridLayoutManager for the RecyclerView to display reviews in a grid format (1 column in this case)
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        // Setting up the adapter for the RecyclerView with the reviews data
        // The reviews are fetched from the selectedOrganizer object
        ReviewAdapter adapter = new ReviewAdapter(selectedOrganizer.getReview());
        reviewsRecyclerView.setAdapter(adapter); // Attaching the adapter to the RecyclerView
    }
}
