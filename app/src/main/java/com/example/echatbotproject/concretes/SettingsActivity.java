package com.example.echatbotproject.concretes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.echatbotproject.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    // Properties
    private Switch switchDarkMode;
    private RadioGroup radioGroupClearHistory;
    private RadioButton radio30Days;
    private RadioButton radio60Days;
    private RadioButton radio90Days;
    private RadioButton radioNever;
    private Switch switchStayLoggedIn;
    private Button buttonClearHistoryNow;
    private Button buttonLogout;
    private Button buttonSave;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        switchDarkMode = findViewById(R.id.switchDarkMode);
        radioGroupClearHistory = findViewById(R.id.radioGroupClearHistory);
        radio30Days = findViewById(R.id.radio30Days);
        radio60Days = findViewById(R.id.radio60Days);
        radio90Days = findViewById(R.id.radio90Days);
        radioNever = findViewById(R.id.radioNever);
        switchStayLoggedIn = findViewById(R.id.switchStayLoggedIn);
        buttonClearHistoryNow = findViewById(R.id.buttonClearHistoryNow);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonSave = findViewById(R.id.buttonSave);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        // Load saved settings
        loadSettings();

        // Initialize Event Listeners
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Handle dark mode toggle logic
            sharedPreferences.edit().putBoolean("darkMode", isChecked).apply();
            // TODO: Apply dark mode theme immediately
        });

        buttonClearHistoryNow.setOnClickListener(v -> {
            // Set a flag to tell ChatbotActivity to clear history on resume
            sharedPreferences.edit().putBoolean("clearHistoryNow", true).apply();
            Toast.makeText(this, "Chat history will be cleared when you return to the chat.", Toast.LENGTH_SHORT).show();
        });

        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        });

        buttonSave.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadSettings() {
        // Load dark mode preference
        boolean darkMode = sharedPreferences.getBoolean("darkMode", false);
        switchDarkMode.setChecked(darkMode);
        // TODO: Apply dark mode theme based on preference

        // Load clear history preference
        String clearHistoryInterval = sharedPreferences.getString("clearHistoryInterval", "never");
        switch (clearHistoryInterval) {
            case "30":
                radio30Days.setChecked(true);
                break;
            case "60":
                radio60Days.setChecked(true);
                break;
            case "90":
                radio90Days.setChecked(true);
                break;
            case "never":
            default:
                radioNever.setChecked(true);
                break;
        }

        // Load stay logged in preference
        boolean stayLoggedIn = sharedPreferences.getBoolean("stayLoggedIn", true); // Default to true
        switchStayLoggedIn.setChecked(stayLoggedIn);
        // TODO: Implement actual stay logged in logic if needed beyond Firebase default
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkMode", switchDarkMode.isChecked());

        String clearHistoryInterval = "never";
        if (radio30Days.isChecked()) {
            clearHistoryInterval = "30";
        } else if (radio60Days.isChecked()) {
            clearHistoryInterval = "60";
        } else if (radio90Days.isChecked()) {
            clearHistoryInterval = "90";
        }
        editor.putString("clearHistoryInterval", clearHistoryInterval);

        editor.putBoolean("stayLoggedIn", switchStayLoggedIn.isChecked());

        editor.apply();
    }
}