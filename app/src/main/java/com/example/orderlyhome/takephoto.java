package com.example.orderlyhome;

import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;
import static android.Manifest.permission.CAMERA;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class takephoto extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_CODE = 100;
    private ImageView accessCamera;
    private Uri currentImageUri;
    private ActivityResultLauncher<Uri> takePictureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.takephoto);  // your XML file

        accessCamera = findViewById(R.id.accesscamera);

        // 1) Prepare the TakePicture contract
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success && currentImageUri != null) {
                        // Show it in the ImageView
                        accessCamera.setImageURI(currentImageUri);
                        // 2) Return it to whoever launched us (e.g. chat)
                        Intent data = new Intent();
                        data.putExtra("imageUri", currentImageUri.toString());
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        Toast.makeText(this, "Photo was not saved", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // 3) Click → request permissions or capture
        accessCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{ CAMERA, ACCESS_MEDIA_LOCATION },
                        REQUEST_PERMISSIONS_CODE
                );
            } else {
                captureImage();
            }
        });
    }

    private void captureImage() {
        // Create a content URI so the camera app can write into it
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,       "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        Uri uri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        );
        if (uri != null) {
            currentImageUri = uri;
            takePictureLauncher.launch(uri);
        } else {
            Toast.makeText(this, "Unable to create image URI", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        // **Important**: let the superclass handle its part, then check ours
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            boolean cameraOk   = ContextCompat.checkSelfPermission(this, CAMERA)
                    == PackageManager.PERMISSION_GRANTED;
            boolean mediaOk    = ContextCompat.checkSelfPermission(this, ACCESS_MEDIA_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
            if (cameraOk && mediaOk) {
                captureImage();
            } else {
                Toast.makeText(
                        this,
                        "Camera + media‑location permission are required",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
