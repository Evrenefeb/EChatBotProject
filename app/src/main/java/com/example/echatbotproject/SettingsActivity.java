package com.example.echatbotproject;

import androidx.annotation.Nullable;

// Importing base activity class
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity{

    // Properties
    private Switch switchDarkMode;
    private Button buttonClearHistory;
    private Button buttonLogout;
    private Button buttonSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        switchDarkMode = findViewById(R.id.switchDarkMode);
        buttonClearHistory = findViewById(R.id.buttonClearHistory);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonSave = findViewById(R.id.buttonSave);

        // Initialize Event Listeners
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Handle dark mode toggle
        });

        buttonClearHistory.setOnClickListener(v -> {
            // TODO: Handle clear history logic
        });

        buttonLogout.setOnClickListener(v -> {
            // TODO: Handle logout logic
            // ...
            Intent intent = new Intent(SettingsActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        });

        buttonSave.setOnClickListener(v -> {
            // TODO: Handle save settings logic
        });

        // TODO: Load saved settings

    }
}
