package com.example.orderlyhome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class loginwithGoogle extends AppCompatActivity {
    private static final String TAG = "LoginWithGoogle";
    // ← your server‑client ID
    private static final String SERVER_CLIENT_ID =
            "1028049242025-7uq996pkkv86bulse3hn96m9i7rp9jee.apps.googleusercontent.com";

    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwithgoogle);

        // 1) Configure Google Sign‑In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(SERVER_CLIENT_ID)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 2) Register launcher to handle the sign‑in Intent
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    } else {
                        Toast.makeText(this, "Sign‑in canceled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 3) Wire up your button
        Button signupBtn = findViewById(R.id.signup);
        signupBtn.setOnClickListener(v ->
                signInLauncher.launch(mGoogleSignInClient.getSignInIntent())
        );
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acct = task.getResult(ApiException.class);
            Toast.makeText(this,
                    "Welcome, " + acct.getDisplayName() + "!",
                    Toast.LENGTH_SHORT).show();

            // Launch MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } catch (ApiException e) {
            Log.w(TAG, "signInFailed: " + e.getStatusCode(), e);
            Toast.makeText(this,
                    "Sign‑in failed: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
