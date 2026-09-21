package com.example.tripbuddy.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripbuddy.R;
import com.example.tripbuddy.utils.RewardsManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class BudgetActivity extends AppCompatActivity {
    private EditText customExpenseInput, mealsExpenseInput, destinationInput, notesInput;
    private EditText startDateInput, endDateInput;
    private TextView subtotalText, discountText, totalText;
    private Button saveTripBtn, confirmTripBtn;
    private CheckBox checkboxSightseeing, checkboxHiking, checkboxDining, checkboxMuseum;
    private double subtotal = 0.0;
    private SharedPreferences prefs;
    private Calendar startDateCalendar = Calendar.getInstance();
    private Calendar endDateCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_budget);

        prefs = getSharedPreferences("TripPrefs", MODE_PRIVATE);

        destinationInput = findViewById(R.id.destinationInput);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        notesInput = findViewById(R.id.notesInput);
        customExpenseInput = findViewById(R.id.customExpenseInput);
        mealsExpenseInput = findViewById(R.id.mealsExpenseInput);

        subtotalText = findViewById(R.id.subtotalText);
        discountText = findViewById(R.id.discountText);
        totalText = findViewById(R.id.totalText);

        saveTripBtn = findViewById(R.id.saveTripBtn);
        confirmTripBtn = findViewById(R.id.confirmTripBtn);

        checkboxSightseeing = findViewById(R.id.checkboxSightseeing);
        checkboxHiking = findViewById(R.id.checkboxHiking);
        checkboxDining = findViewById(R.id.checkboxDining);
        checkboxMuseum = findViewById(R.id.checkboxMuseum);

        startDateInput.setOnClickListener(v -> showDatePicker(true));
        endDateInput.setOnClickListener(v -> showDatePicker(false));

        CompoundButton.OnCheckedChangeListener activityCheckListener = (buttonView, isChecked) -> recalculateSubtotal();
        checkboxSightseeing.setOnCheckedChangeListener(activityCheckListener);
        checkboxHiking.setOnCheckedChangeListener(activityCheckListener);
        checkboxDining.setOnCheckedChangeListener(activityCheckListener);
        checkboxMuseum.setOnCheckedChangeListener(activityCheckListener);

        customExpenseInput.addTextChangedListener(budgetWatcher);
        mealsExpenseInput.addTextChangedListener(budgetWatcher);

        saveTripBtn.setOnClickListener(v -> saveTrip());
        confirmTripBtn.setOnClickListener(v -> openSummary());
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = isStartDate ? startDateCalendar : endDateCalendar;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                    if (isStartDate) {
                        startDateInput.setText(date);
                        if (!endDateInput.getText().toString().isEmpty() &&
                                endDateCalendar.before(startDateCalendar)) {
                            endDateInput.setText("");
                        }
                    } else {
                        if (calendar.before(startDateCalendar)) {
                            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        endDateInput.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private final TextWatcher budgetWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { recalculateSubtotal(); }
    };

    private void recalculateSubtotal() {
        subtotal = 0.0;

        if (checkboxSightseeing.isChecked()) subtotal += 500.0;
        if (checkboxHiking.isChecked()) subtotal += 300.0;
        if (checkboxDining.isChecked()) subtotal += 400.0;
        if (checkboxMuseum.isChecked()) subtotal += 250.0;

        try {
            String custom = customExpenseInput.getText().toString().trim();
            if (!custom.isEmpty()) subtotal += Double.parseDouble(custom);
        } catch (NumberFormatException ignored) {}

        try {
            String meals = mealsExpenseInput.getText().toString().trim();
            if (!meals.isEmpty()) subtotal += Double.parseDouble(meals);
        } catch (NumberFormatException ignored) {}

        updateBudget();
    }

    private void updateBudget() {
        RewardsManager rm = new RewardsManager(this);
        double discount = rm.isEligibleForDiscount() ? subtotal * 0.10 : 0;
        double total = subtotal - discount;

        subtotalText.setText("Subtotal: R" + String.format("%.2f", subtotal));
        discountText.setText("Discount: R" + String.format("%.2f", discount));
        totalText.setText("Total: R" + String.format("%.2f", total));
    }

    private void saveTrip() {
        String destination = destinationInput.getText().toString().trim();
        String startDate = startDateInput.getText().toString().trim();
        String endDate = endDateInput.getText().toString().trim();
        String notes = notesInput.getText().toString().trim();

        if (destination.isEmpty() || notes.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RewardsManager rm = new RewardsManager(this);
        double discount = rm.isEligibleForDiscount() ? subtotal * 0.10 : 0;
        double total = subtotal - discount;

        JSONObject trip = new JSONObject();
        try {
            trip.put("destination", destination);
            trip.put("startDate", startDate);
            trip.put("endDate", endDate);
            trip.put("notes", notes);
            trip.put("subtotal", subtotal);
            trip.put("discount", discount);
            trip.put("total", total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveTripToHistory(trip);

        rm.incrementTripCount();

        Toast.makeText(this, "Trip Saved!", Toast.LENGTH_SHORT).show();
    }

    private void saveTripToHistory(JSONObject trip) {
        String json = prefs.getString("tripHistory", "[]");
        try {
            JSONArray trips = new JSONArray(json);
            trips.put(trip);
            prefs.edit().putString("tripHistory", trips.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openSummary() {
        startActivity(new Intent(this, SummaryActivity.class));
    }
}