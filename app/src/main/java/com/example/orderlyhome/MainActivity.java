package com.example.orderlyhome;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // List to hold all organizers and a variable to track the current index
    private List<Organizer> organizers;
    private int currentIndex = 0;

    // Views for the main organizer info
    public ImageView imageView;
    public ImageView starsImageView;
    public TextView nameTextView, locationTextView, feeTextView;
    public Button moreInfoButton;
    public ImageButton noButton, yesButton;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge layout for modern phones
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find Views by their IDs in the layout XML file
        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.name1);
        locationTextView = findViewById(R.id.place1);
        feeTextView = findViewById(R.id.fee1);
        moreInfoButton = findViewById(R.id.moreInfo);
        noButton = findViewById(R.id.no);
        yesButton = findViewById(R.id.yes);
        starsImageView = findViewById(R.id.stars5);

        // Create a list of organizers with their details and reviews
        organizers = new ArrayList<>();

        // Reviews for Maayan Levy
        List<reviews> reviewsForMaayan = new ArrayList<>();
//        reviewsForMaayan.add(new reviews("4.5 stars", R.drawable.n_logo, "Noa", "Maayan is a very sweet woman and she does her job well, I just wished she didn’t whistle as much."));
//        reviewsForMaayan.add(new reviews("5 stars", R.drawable.o_logo, "Olivia", "I am so happy I found her! She's the best. Every time I call her she does the job so well."));
//        reviewsForMaayan.add(new reviews("5 stars", R.drawable.k_logo, "Karen", "I hired her and was so pleasantly surprised. She was so fast and attentive. She even baked cookies for my kids."));
//        reviewsForMaayan.add(new reviews("4 stars", R.drawable.d_logo, "Daniel", "She arrived to the job a little late but did a good job and was efficient!"));
//
//        // Create Maayan's organizer object and add it to the list
        organizers.add(new Organizer("Maayan Levy", "Netanya", "₪150/hour",
                "Hello, I am Maayan, I’m a pensioner. When I’m not with my grandchildren i like to tidy things up around the house, it relaxes me.", 45,
                R.drawable.user_a, R.drawable.stars5, R.drawable.banda0, R.drawable.banda2, reviewsForMaayan));

//        // Create a list of reviews for Annabel Smith
        List<reviews> reviewsForAnnabel = new ArrayList<>();
//        reviewsForAnnabel.add(new reviews("3 stars", R.drawable.g_logo, "Grace", "She was nice but she had to leave earlier, she says it was for a family emergency..."));
//        reviewsForAnnabel.add(new reviews("4.5 stars", R.drawable.t_logo, "Tyra", "She was so good, she even finished before the time and offered to take my dog out on a walk."));
//        reviewsForAnnabel.add(new reviews("5 stars", R.drawable.y_logo, "Yael", "She is so good! You can tell she is experienced. She even helped me shower my 2 months old daughter- as a new mom that was helpful."));
//        reviewsForAnnabel.add(new reviews("3.5 stars", R.drawable.s_logo, "Sarah", "Good, but a bit slow."));
//
//        // Create Annabel's organizer object and add it to the list
        organizers.add(new Organizer("Annabel Smith", "Raanana", "₪100/hour",
                "Hello, I am Annabel, I work as a receptionist at a hotel. I am a very organized person and I like to help others be as organized as me.", 38,
                R.drawable.user_b, R.drawable.stars4, R.drawable.banda3, R.drawable.banda4, reviewsForAnnabel));
//
//        // Create a list of reviews for Sarah Katz
//        List<reviews> reviewsForSarah = new ArrayList<>();
//        reviewsForSarah.add(new reviews("2.5 stars", R.drawable.h_logo, "Haley", "She was ok I guess. Did what she needed to do."));
//        reviewsForSarah.add(new reviews("5 stars", R.drawable.i_logo, "Iris", "Exceeded my expectations."));
//        reviewsForSarah.add(new reviews("4 stars", R.drawable.m_logo, "Monica", "Very attentive and detailed."));
//        reviewsForSarah.add(new reviews("4.5 stars", R.drawable.r_logo, "Robert", "It took a few days to get everything done but Sarah is very detail-oriented and won't miss a thing!"));
//
//        // Create Sarah's organizer object and add it to the list
//        organizers.add(new Organizer("Sarah Katz", "Jerusalem", "₪180/hour",
//                "Since I was a little girl I've loved organizing. I am a perfectionist and a control freak, so you will definitely be happy with my work. I PROMISE YOU!", 28,
//                R.drawable.user_c, R.drawable.stars5, R.drawable.banda5, R.drawable.banda6, reviewsForSarah));

        // Display the first organizer's information
        showOrganizer(currentIndex);

        // Set up listener for "X/no" button to go to the next organizer
        noButton.setOnClickListener(view -> {
            currentIndex++;
            if (currentIndex >= organizers.size()) {
                currentIndex = 0; // If we reach the end of the list, loop back to the first organizer
            }
            showOrganizer(currentIndex); // Show the updated organizer
        });

        // Set up listener for "yes" button to confirm the selection of the current organizer
        yesButton.setOnClickListener(view -> {
            Toast.makeText(MainActivity.this,
                    "Organizer " + organizers.get(currentIndex).getName() + " selected!",
                    Toast.LENGTH_SHORT).show(); // Show confirmation message
        });

        // Set up listener for the "More Info" button to open the detailed profile screen of the current organizer
        moreInfoButton.setOnClickListener(view -> {
//            throw new RuntimeException("Test Crash");
            Intent intent = new Intent(MainActivity.this, Screen2_MoreInfo.class);
            Organizer currentOrganizer = organizers.get(currentIndex); // Get the current organizer
            intent.putExtra("selectedOrganizer", currentOrganizer); // Pass the selected organizer to the next activity
            startActivity(intent); // Start the new activity
        });
    }

    // Update the UI with the current organizer's information
    public void showOrganizer(int index) {
        Organizer organizer = organizers.get(index); // Get the organizer at the current index
        nameTextView.setText(organizer.getName()); // Set name
        locationTextView.setText(organizer.getLocation()); // Set location
        feeTextView.setText(organizer.getFee()); // Set fee
        imageView.setImageResource(organizer.getImageResId()); // Set the image resource
        starsImageView.setImageResource(organizer.getStarsImageResId()); // Set the stars image
    }
}
