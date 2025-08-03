package com.example.orderlyhome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Screen2_MoreInfo extends AppCompatActivity {
    private ReviewAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2_more_info);

        // 1) Read the Organizer we were given
        final Organizer selectedOrganizer =
                (Organizer) getIntent().getSerializableExtra("selectedOrganizer");
        final String organizerId = selectedOrganizer.getName();

        // 2) Populate the profile views
        ((TextView) findViewById(R.id.name_a))
                .setText(selectedOrganizer.getName());
        ((TextView) findViewById(R.id.place_a))
                .setText(selectedOrganizer.getLocation());
        ((TextView) findViewById(R.id.fee_a))
                .setText(selectedOrganizer.getFee());
        ((TextView) findViewById(R.id.age_a))
                .setText("Age: " + selectedOrganizer.getAge());
        ((TextView) findViewById(R.id.details_a))
                .setText(selectedOrganizer.getDescription());
        ((ImageView) findViewById(R.id.displaypicture_a))
                .setImageResource(selectedOrganizer.getImageResId());
        ((ImageView) findViewById(R.id.portfoliopic1))
                .setImageResource(selectedOrganizer.getPortfolioPic1ResId());
        ((ImageView) findViewById(R.id.portfoliopic2))
                .setImageResource(selectedOrganizer.getPortfolioPic2ResId());

        // 3) Firestore‐backed ReviewAdapter
        rv = findViewById(R.id.reviewsRecyclerView);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new ReviewAdapter(organizerId);
        rv.setAdapter(adapter);

        // 4) Launch AddReviewActivity with organizerId
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        ActivityResultLauncher<Intent> addReviewLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                reviews newR = (reviews) result.getData()
                                        .getSerializableExtra("review");
                                adapter.addReviews(newR);
                                rv.scrollToPosition(adapter.getItemCount() - 1);
                            }
                        }
                );
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, AddReviewActivity.class);
            i.putExtra("organizerId", organizerId);
            addReviewLauncher.launch(i);
        });

        // 5) Message button (unchanged)
        Button messageBtn = findViewById(R.id.btn_message);
        messageBtn.setOnClickListener(v -> {
            Intent chatIntent = new Intent(this, chat.class);
            chatIntent.putExtra("selectedOrganizer", selectedOrganizer);
            startActivity(chatIntent);
        });

        // 6) Home button: go back to MainActivity
        ImageButton homeBtn = findViewById(R.id.homebutton2);
        homeBtn.setOnClickListener(v -> {
            // simply finish() to return to whatever launched this screen
            finish();

            // OR, to always start a fresh MainActivity, uncomment:
            // Intent back = new Intent(Screen2_MoreInfo.this, MainActivity.class);
            // startActivity(back);
            // finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.cleanup();  // stop Firestore listener
    }
}