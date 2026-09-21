package com.example.tripbuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tripbuddy.R;
import com.example.tripbuddy.utils.SessionManager;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button gallery = findViewById(R.id.galleryBtn);
        Button memories = findViewById(R.id.memoriesBtn);
        Button budget = findViewById(R.id.budgetBtn);
        Button logout = findViewById(R.id.logoutBtn);
        Button settings = findViewById(R.id.settingsBtn);

        gallery.setOnClickListener(v -> startActivity(new Intent(this, GalleryActivity.class)));
        memories.setOnClickListener(v -> startActivity(new Intent(this, MemoriesActivity.class)));
        budget.setOnClickListener(v -> startActivity(new Intent(this, BudgetActivity.class)));

        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        logout.setOnClickListener(v -> {
            new SessionManager(HomeActivity.this).logout();
            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
