package com.example.orderlyhome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddReviewActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        // 1) Read which organizer we’re reviewing
        String organizerId = getIntent().getStringExtra("organizerId");

        EditText et_name   = findViewById(R.id.et_name);
        EditText et_rating = findViewById(R.id.et_rating);
        EditText et_review = findViewById(R.id.et_review);
        Button submit      = findViewById(R.id.submit_btn);

        submit.setOnClickListener(view -> {
            // 2) Build and tag the review
            reviews r = new reviews(
                    et_rating.getText().toString(),
                    et_name.getText().toString(),
                    et_review.getText().toString()
            );
            r.setOrganizerId(organizerId);

            // 3) Save to Firestore
            db.collection("Reviews")
                    .document(r.getID())
                    .set(r.getAsMap());

            // 4) Return it in the result
            Intent result = new Intent();
            result.putExtra("review", r);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
