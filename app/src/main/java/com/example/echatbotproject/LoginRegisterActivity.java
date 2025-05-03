package com.example.echatbotproject;

import androidx.annotation.Nullable;

// Importing base activity class
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.content.Intent;

public class LoginRegisterActivity extends AppCompatActivity{

    // Properties
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private ProgressBar progressBar;

    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Event Listeners
        buttonLogin.setOnClickListener(v -> {
            loginOnClickEvent();
        });

        buttonRegister.setOnClickListener(v -> {
            registerOnClickEvent();
        });
    }

    // Event Listeners

    private void loginOnClickEvent(){
        // TODO: Handle login logic
        // ...
        // If login is successful:
        Intent intent = new Intent(LoginRegisterActivity.this, ChatbotActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerOnClickEvent(){
        // TODO: Handle registration logic here
        // ...
        // If registration is successful:
        Intent intent = new Intent(LoginRegisterActivity.this, ChatbotActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO: Implement Methods for handling login and registration
}
