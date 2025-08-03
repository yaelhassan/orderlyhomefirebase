package com.example.orderlyhome;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class book extends AppCompatActivity {

    private TextView selectedTimeSlot = null;
    private TextView selectedDay      = null;

    // Organizer → which day‑IDs they work
    private static final Map<String, List<Integer>> DAYS_BY_ORG = new HashMap<>();
    // Organizer → which time‑slot IDs they offer
    private static final Map<String, List<Integer>> TIMES_BY_ORG = new HashMap<>();

    static {
        DAYS_BY_ORG.put("Maayan Levy", Arrays.asList(
                R.id.wednesday, R.id.thursday, R.id.friday
        ));
        DAYS_BY_ORG.put("Annabel Smith", Arrays.asList(
                R.id.monday, R.id.tuesday
        ));

        TIMES_BY_ORG.put("Maayan Levy", Arrays.asList(
                R.id.time1, R.id.time3
        ));
        TIMES_BY_ORG.put("Annabel Smith", Arrays.asList(
                R.id.time2, R.id.time4
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);

        // 1) Which organizer?
        String name = getIntent().getStringExtra("name");
        if (name == null) name = "Maayan Levy";

        // 2) Set dynamic title
        TextView titleText = findViewById(R.id.book_title);
        titleText.setText("Book with " + name);

        // 3) Highlight & wire up available days
        List<Integer> goodDays = DAYS_BY_ORG.getOrDefault(name, List.of());
        int[] allDays = {
                R.id.sunday, R.id.monday, R.id.tuesday,
                R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday
        };
        for (int dayId : allDays) {
            TextView day = findViewById(dayId);
            boolean available = goodDays.contains(dayId);

            // always use your circle drawable
            day.setBackgroundResource(R.drawable.day_selected);
            day.setBackgroundTintList(
                    available
                            ? ColorStateList.valueOf(Color.parseColor("#FFFFFF"))  // white fill
                            : ColorStateList.valueOf(Color.parseColor("#F8F1F0"))  // grey fill
            );
            day.setTextColor(
                    available
                            ? Color.parseColor("#D28850")  // orange text
                            : Color.parseColor("#C0B8AF")  // grey text
            );
            day.setClickable(available);

            if (available) {
                day.setOnClickListener(v -> {
                    // un‑select previous
                    if (selectedDay != null) {
                        selectedDay.setBackgroundTintList(
                                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        );
                        selectedDay.setTextColor(Color.parseColor("#D28850"));
                    }
                    // select this day: orange fill + white text
                    TextView clicked = (TextView) v;
                    clicked.setBackgroundTintList(
                            ColorStateList.valueOf(Color.parseColor("#D58F6B"))
                    );
                    clicked.setTextColor(Color.parseColor("#F8F1F0"));
                    selectedDay = clicked;
                });
            }
        }

        // 4) Enable/fade & wire up time slots
        List<Integer> goodTimes = TIMES_BY_ORG.getOrDefault(name, List.of());
        FlexboxLayout timeSlotContainer = findViewById(R.id.timeSlotContainer);
        for (int i = 0; i < timeSlotContainer.getChildCount(); i++) {
            View child = timeSlotContainer.getChildAt(i);
            if (!(child instanceof TextView)) continue;

            TextView slot = (TextView) child;
            boolean ok = goodTimes.contains(slot.getId());

            // keep your rounded selector
            slot.setBackgroundResource(R.drawable.time_slot_selector);
            slot.setBackgroundTintList(
                    ok
                            ? ColorStateList.valueOf(Color.parseColor("#FFFFFF"))  // white
                            : ColorStateList.valueOf(Color.parseColor("#F8F1F0"))  // grey
            );
            slot.setTextColor(
                    ok
                            ? Color.parseColor("#D28850")
                            : Color.parseColor("#9A7F66")
            );
            slot.setClickable(ok);

            if (ok) {
                slot.setOnClickListener(v -> {
                    // un‑select previous
                    if (selectedTimeSlot != null) {
                        selectedTimeSlot.setBackgroundTintList(
                                ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        );
                        selectedTimeSlot.setTextColor(Color.parseColor("#D28850"));
                    }
                    // select this slot: orange fill + white text
                    TextView clicked = (TextView) v;
                    clicked.setBackgroundTintList(
                            ColorStateList.valueOf(Color.parseColor("#D58F6B"))
                    );
                    clicked.setTextColor(Color.parseColor("#F8F1F0"));
                    selectedTimeSlot = clicked;
                });
            }
        }

        // 5) Back arrow
        findViewById(R.id.backbuttonbook)
                .setOnClickListener(v -> finish());

        // 6) **Continue to Payment**:
        TextView continueBtn = findViewById(R.id.continueButton);
        continueBtn.setOnClickListener(v -> {
            // Make sure the user chose a day & time
            if (selectedDay == null || selectedTimeSlot == null) {
                // you can show a Toast or disable the button until both are picked
                return;
            }

            // Gather the four extras
            String hoursFrom    = selectedTimeSlot.getText().toString();
            String hoursTo      = "—"; // if you have separate end‑time slots, use that instead
            String totalPayment = "";  // you should calculate fee * hours

            // Launch Payment screen
            Intent i = new Intent(book.this, Payment.class);

            startActivity(i);
        });
    }
}
