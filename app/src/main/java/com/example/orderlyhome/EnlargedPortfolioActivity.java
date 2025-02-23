package com.example.orderlyhome;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class EnlargedPortfolioActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_enlarged);

        // Reference the ImageView directly using its ID
        ImageView enlargedImageView = findViewById(R.id.portfolioPic1);

        // Retrieve the image resource ID and transition name from the Intent
        int imageResId = getIntent().getIntExtra("imageResId", 0);
        String transitionName = getIntent().getStringExtra("transitionName");

        if (imageResId != 0) {
            enlargedImageView.setImageResource(imageResId);
        }
        if (transitionName != null) {
            // Set the same transition name for the shared element animation
            enlargedImageView.setTransitionName(transitionName);
        }
    }
}