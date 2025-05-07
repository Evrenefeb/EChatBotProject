package com.example.echatbotproject.concretes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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

    private Switch switchDarkMode;
    private RadioGroup radioGroupClearHistory;
    private RadioButton radio1Day;
    private RadioButton radio3Days;
    private RadioButton radio5Days;
    private RadioButton radio7Days;
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

        switchDarkMode = findViewById(R.id.switchDarkMode);
        radioGroupClearHistory = findViewById(R.id.radioGroupClearHistory);
        radio1Day = findViewById(R.id.radio1Day);
        radio3Days = findViewById(R.id.radio3Days);
        radio5Days = findViewById(R.id.radio5Days);
        radio7Days = findViewById(R.id.radio7Days);
        radioNever = findViewById(R.id.radioNever);
        switchStayLoggedIn = findViewById(R.id.switchStayLoggedIn);
        buttonClearHistoryNow = findViewById(R.id.buttonClearHistoryNow);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonSave = findViewById(R.id.buttonSave);

        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);

        loadSettings();

        buttonClearHistoryNow.setOnClickListener(v -> {
            sharedPreferences.edit().putBoolean("clearHistoryNow", true).apply();
            Toast.makeText(this, R.string.chat_history_will_be_cleared, Toast.LENGTH_SHORT).show();
        });

        buttonLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        });

        buttonSave.setOnClickListener(v -> {
            saveSettings();
            applyTheme(switchDarkMode.isChecked());
            Toast.makeText(this, R.string.settings_saved, Toast.LENGTH_SHORT).show();
        });
    }

    private void loadSettings() {
        boolean darkMode = sharedPreferences.getBoolean("darkMode", false);
        switchDarkMode.setChecked(darkMode);
        applyTheme(darkMode);

        String clearHistoryInterval = sharedPreferences.getString("clearHistoryInterval", "never");
        switch (clearHistoryInterval) {
            case "1":
                radio1Day.setChecked(true);
                break;
            case "3":
                radio3Days.setChecked(true);
                break;
            case "5":
                radio5Days.setChecked(true);
                break;
            case "7":
                radio7Days.setChecked(true);
                break;
            case "never":
            default:
                radioNever.setChecked(true);
                break;
        }

        boolean stayLoggedIn = sharedPreferences.getBoolean("stayLoggedIn", true);
        switchStayLoggedIn.setChecked(stayLoggedIn);
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("darkMode", switchDarkMode.isChecked());

        String clearHistoryInterval = "never";
        if (radio1Day.isChecked()) {
            clearHistoryInterval = "1";
        } else if (radio3Days.isChecked()) {
            clearHistoryInterval = "3";
        } else if (radio5Days.isChecked()) {
            clearHistoryInterval = "5";
        } else if (radio7Days.isChecked()) {
            clearHistoryInterval = "7";
        }
        editor.putString("clearHistoryInterval", clearHistoryInterval);

        editor.putBoolean("stayLoggedIn", switchStayLoggedIn.isChecked());

        editor.apply();
    }

    private void applyTheme(boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}