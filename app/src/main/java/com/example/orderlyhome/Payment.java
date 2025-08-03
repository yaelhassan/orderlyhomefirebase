package com.example.orderlyhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Payment extends AppCompatActivity {

    // UI references
    private TextView tvPayNow;
    private ImageButton backBtn;
    private EditText etHoursFrom, etHoursTo, etTotalPayment;
    private EditText etCardholderName, etCardNumber, etMonth, etYear, etCvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        // 1) Pull in the passed‑in values
        Intent in = getIntent();
        String organizerName = in.getStringExtra("organizerName");
        String hoursFrom     = in.getStringExtra("hoursFrom");
        String hoursTo       = in.getStringExtra("hoursTo");
        String totalPayment  = in.getStringExtra("totalPayment");

        // 2) Wire up views
        backBtn          = findViewById(R.id.backbuttonpayment);
        tvPayNow         = findViewById(R.id.paynow);
        etHoursFrom      = findViewById(R.id.hours_from);
        etHoursTo        = findViewById(R.id.hours_to);
        etTotalPayment   = findViewById(R.id.payment_total);
        etCardholderName = findViewById(R.id.cardholdername_title);
        etCardNumber     = findViewById(R.id.cardnumber_title);
        etMonth          = findViewById(R.id.month_title);
        etYear           = findViewById(R.id.year_title);
        etCvv            = findViewById(R.id.cvv_title);

        // 3) Pre‑populate if available
        if (hoursFrom != null)    etHoursFrom.setText(hoursFrom);
        if (hoursTo != null)      etHoursTo.setText(hoursTo);
        if (totalPayment != null) etTotalPayment.setText(totalPayment);

        // 4) Back arrow closes this screen
        backBtn.setOnClickListener(v -> finish());

        // 5) PAY NOW: validate, then show confirmation dialog
        tvPayNow.setOnClickListener(v -> {
            String name = etCardholderName.getText().toString().trim();
            String card = etCardNumber.getText().toString().trim();
            String mm   = etMonth.getText().toString().trim();
            String yy   = etYear.getText().toString().trim();
            String cvv  = etCvv.getText().toString().trim();

            if (name.isEmpty() || card.isEmpty() || mm.isEmpty() || yy.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Please complete all payment fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // **Build and show a dialog instead of finishing**
            new AlertDialog.Builder(this)
                    .setTitle("Payment Successful")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // optional: clear form or disable button
                        tvPayNow.setEnabled(false);
                    })
                    .setCancelable(false)
                    .show();
        });
    }
}
