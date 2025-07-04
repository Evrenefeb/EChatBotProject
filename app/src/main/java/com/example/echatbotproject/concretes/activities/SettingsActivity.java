package com.example.echatbotproject.concretes.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.echatbotproject.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    // Properties
    private Switch switchDarkMode;
    private Spinner languageSpinner;
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
    private String currentLanguage = "en";

    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        languageSpinner = findViewById(R.id.languageSpinner);
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

        // Initialize the language Spinner
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.languages, // Defined in values/arrays.xml and values-tr/arrays.xml
                android.R.layout.simple_spinner_item
        );
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                String langCode = "en";
                if (selectedLanguage.equals(getString(R.string.turkish))) {
                    langCode = "tr";
                }

                if (!currentLanguage.equals(langCode)) {
                    currentLanguage = langCode;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", currentLanguage);
                    editor.apply();
                    setLocale(currentLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

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

    // Private Methods

    private void loadSettings() {
        boolean darkMode = sharedPreferences.getBoolean("darkMode", false);
        switchDarkMode.setChecked(darkMode);
        applyTheme(darkMode);

        String language = sharedPreferences.getString("language", "en");
        currentLanguage = language;
        int spinnerPosition = 0;

        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) languageSpinner.getAdapter();
        if (adapter != null) {
            if (language.equals("tr")) {
                spinnerPosition = adapter.getPosition(getString(R.string.turkish));
            } else {
                spinnerPosition = adapter.getPosition(getString(R.string.english));
            }
            if (spinnerPosition == -1) {
                spinnerPosition = 0; // Default to English if not found
            }
            languageSpinner.setSelection(spinnerPosition);
        }
        // No need to apply locale here - it will apply when app starts

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

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        // Create and set LocaleList
        LocaleListCompat appLocales = LocaleListCompat.create(locale);
        AppCompatDelegate.setApplicationLocales(appLocales);
        updateConfiguration(this, locale);
        recreate();
    }

    private void updateConfiguration(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(locale);

        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);

        context.createConfigurationContext(configuration);
    }
}