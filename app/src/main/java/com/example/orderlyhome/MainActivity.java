package com.example.orderlyhome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // List of all organizers and index of the one currently shown
    private List<Organizer> organizers;
    private int currentIndex = 0;

    // UI refs
    private ImageView imageView, starsImageView;
    private TextView nameTextView, locationTextView, feeTextView;
    private Button moreInfoButton;
    private ImageButton noButton, yesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply edge‑to‑edge insets (root must have android:id="@+id/main")
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.chat),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        // Find views
        imageView        = findViewById(R.id.imageView);
        starsImageView   = findViewById(R.id.stars5);
        nameTextView     = findViewById(R.id.name1);
        locationTextView = findViewById(R.id.place1);
        feeTextView      = findViewById(R.id.fee1);
        moreInfoButton   = findViewById(R.id.moreInfo);
        noButton         = findViewById(R.id.no);
        yesButton        = findViewById(R.id.yes);

        // Build your organizers
        organizers = new ArrayList<>();

        List<reviews> reviewsForMaayan = new ArrayList<>();
        reviewsForMaayan.add(new reviews("4.5 stars","Noa","Very sweet and thorough"));
        reviewsForMaayan.add(new reviews("5 stars","Olivia","She's the best!"));
        organizers.add(new Organizer(
                "Maayan Levy", "Netanya", "₪150/hour",
                "Hello! I am Maayan, I’m a pensioner. When I’m not with my grandchildren I like to tidy things up around the house—it relaxes me.",
                45,
                R.drawable.user_a, R.drawable.stars5,
                R.drawable.banda0, R.drawable.banda2,
                reviewsForMaayan
        ));

        List<reviews> reviewsForAnnabel = new ArrayList<>();
        reviewsForAnnabel.add(new reviews("3 stars","Grace","Nice but had to leave early"));
        reviewsForAnnabel.add(new reviews("4.5 stars","Tyra","Very efficient!"));
        organizers.add(new Organizer(
                "Annabel Smith", "Raanana", "₪100/hour",
                "Hello, I am Annabel, I work as a receptionist at a hotel. I'm very organized and love helping others get there too.",
                38,
                R.drawable.user_b, R.drawable.stars4,
                R.drawable.banda3, R.drawable.banda4,
                reviewsForAnnabel
        ));

        // Show the first organizer
        showOrganizer(currentIndex);

        // “No” cy­cles forward
        noButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % organizers.size();
            showOrganizer(currentIndex);
        });

        // **“Yes” (heart) → open Chat** with this organizer
        yesButton.setOnClickListener(v -> {
            Organizer selected = organizers.get(currentIndex);
            Intent chatIntent = new Intent(MainActivity.this, chat.class);
            chatIntent.putExtra("selectedOrganizer", selected);
            startActivity(chatIntent);
        });

        // “More Info” still opens the profile
        moreInfoButton.setOnClickListener(v -> {
            Organizer selected = organizers.get(currentIndex);
            Intent infoIntent = new Intent(MainActivity.this, Screen2_MoreInfo.class);
            infoIntent.putExtra("selectedOrganizer", selected);
            startActivity(infoIntent);
        });
    }

    /** Update UI to show organizer at `index`. */
    private void showOrganizer(int index) {
        Organizer o = organizers.get(index);
        nameTextView.setText(o.getName());
        locationTextView.setText(o.getLocation());
        feeTextView.setText(o.getFee());
        imageView.setImageResource(o.getImageResId());
        starsImageView.setImageResource(o.getStarsImageResId());
    }
}
