package com.example.tripbuddy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.tripbuddy.R;
import com.example.tripbuddy.utils.SessionManager;

public class WelcomeDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("app_settings", MODE_PRIVATE);
        boolean isNightMode = prefs.getBoolean("night_mode", false);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomedashboard);

        new Handler().postDelayed(() -> {
            SessionManager sm = new SessionManager(WelcomeDashboardActivity.this);
            if (sm.isLoggedIn()) {
                startActivity(new Intent(WelcomeDashboardActivity.this, HomeActivity.class));
            } else {
                startActivity(new Intent(WelcomeDashboardActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}
