package com.example.tripbuddy.activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripbuddy.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SummaryActivity extends AppCompatActivity {
    private LinearLayout historyContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        historyContainer = findViewById(R.id.historyContainer);
        Button btnNewTrip = findViewById(R.id.btnNewTrip);
        loadTripHistory();

        btnNewTrip.setOnClickListener(v -> {
            Intent intent = new Intent(SummaryActivity.this, BudgetActivity.class);
            startActivity(intent);
        });
    }
    private void loadTripHistory() {
        SharedPreferences prefs = getSharedPreferences("TripPrefs", MODE_PRIVATE);
        String json = prefs.getString("tripHistory", "[]");
        try {
            JSONArray trips = new JSONArray(json);
            for (int i = 0; i < trips.length(); i++) {
                JSONObject trip = trips.getJSONObject(i);
                TextView tv = new TextView(this);
                tv.setText(formatTrip(trip));
                tv.setPadding(0, 16, 0, 16);
                historyContainer.addView(tv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private String formatTrip(JSONObject trip) throws JSONException {
        String dest = trip.getString("destination");
        String start = trip.getString("startDate");
        String end = trip.getString("endDate");
        String notes = trip.getString("notes");
        double subtotal = trip.getDouble("subtotal");
        double discount = trip.getDouble("discount");
        double total = trip.getDouble("total");
        return "Destination: " + dest +
                "\nDates: " + start + " - " + end +
                "\nNotes: " + notes +
                "\nSubtotal: R" + String.format("%.2f", subtotal) +
                "\nDiscount: R" + String.format("%.2f", discount) +
                "\nTotal: R" + String.format("%.2f", total);
    }
}