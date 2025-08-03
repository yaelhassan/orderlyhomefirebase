package com.example.orderlyhome;

import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.CAMERA;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class chat extends AppCompatActivity {
    private FirebaseFirestore db;
    private MessageAdapter adapter;

    // Top‑bar & bottom nav buttons
    private ImageButton backBtn, menuBtn, homeBtn, chatBtn, profileBtn;
    // Chat header
    private TextView nameChat;
    private ImageView peerImage;
    // Message input
    private EditText etChat;
    private ImageButton cameraBtn, sendBtn;
    // Message list
    private RecyclerView messagesRecyclerView;
    // Book button
    private Button bookBtn;

    // For taking pictures
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private Uri currentImageUri;
    private static final int REQUEST_PERMISSIONS_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        // Edge‑to‑edge insets on the root view
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.chatmain),
                (v, insets) -> {
                    Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
                    return insets;
                }
        );

        // 1) Grab the Organizer
        final Organizer selectedOrganizer =
                (Organizer) getIntent().getSerializableExtra("selectedOrganizer");
        final String organizerId = selectedOrganizer.getName();

        // 2) Find all your views
        backBtn              = findViewById(R.id.backbuttonchat);
        menuBtn              = findViewById(R.id.menu4);
        homeBtn              = findViewById(R.id.homebutton3);
        chatBtn              = findViewById(R.id.chat3);
        profileBtn           = findViewById(R.id.profile3);
        nameChat             = findViewById(R.id.name_chat);
        peerImage            = findViewById(R.id.imageView2);
        etChat               = findViewById(R.id.et_chat);
        cameraBtn            = findViewById(R.id.camera);
        sendBtn              = findViewById(R.id.send);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        bookBtn              = findViewById(R.id.book_btn);

        // 3) Header setup
        nameChat.setText(selectedOrganizer.getName());
        peerImage.setImageResource(selectedOrganizer.getImageResId());

        // 4) Firestore + RecyclerView
        db = FirebaseFirestore.getInstance();
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(organizerId);
        messagesRecyclerView.setAdapter(adapter);

        // 5) Send text message
        sendBtn.setOnClickListener(v -> {
            String text = etChat.getText().toString().trim();
            if (text.isEmpty()) return;
            long ts = System.currentTimeMillis();
            Message msg = new Message(text, true, ts);
            adapter.addMessage(msg);
            messagesRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
            Map<String,Object> map = msg.getAsMap();
            map.put("organizerId", organizerId);
            db.collection("Messages").document(msg.getId()).set(map);
            etChat.setText("");
        });

        // 6) Camera launcher setup
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result && currentImageUri != null) {
                        long ts = System.currentTimeMillis();
                        Message imgMsg = new Message(
                                currentImageUri.toString(),
                                true, ts, true
                        );
                        adapter.addMessage(imgMsg);
                        messagesRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        Map<String,Object> map = imgMsg.getAsMap();
                        map.put("organizerId", organizerId);
                        db.collection("Messages").document(imgMsg.getId()).set(map);
                    }
                }
        );
        cameraBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, ACCESS_MEDIA_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{CAMERA, ACCESS_MEDIA_LOCATION},
                        REQUEST_PERMISSIONS_CODE
                );
            } else {
                captureImage();
            }
        });

        // 7) Book button action – pass the organizer’s name under the key "name"
        bookBtn.setOnClickListener(v -> {
            Intent i = new Intent(chat.this, book.class);
            i.putExtra("name", organizerId);
            startActivity(i);
        });

        // 8) Back arrow → pop this screen
        backBtn.setOnClickListener(v -> finish());

        // 9) Home button → clear stack and launch MainActivity
        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(chat.this, MainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
            );
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            boolean ok = true;
            for (int res : grantResults) ok &= (res == PackageManager.PERMISSION_GRANTED);
            if (ok) captureImage();
            else Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
        }
    }

    private void captureImage() {
        Uri uri = createImageUri();
        if (uri == null) return;
        currentImageUri = uri;
        takePictureLauncher.launch(uri);
    }

    private Uri createImageUri() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "ChatImage");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image from chat camera");
        return getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.cleanup();
    }
}
