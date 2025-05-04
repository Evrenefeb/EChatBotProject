package com.example.echatbotproject;

import androidx.annotation.Nullable;

// Importing base activity class
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.content.Intent;
import android.widget.Toast;

// Importing firebase auth
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginRegisterActivity extends AppCompatActivity{

    // Properties
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


        // Initialize Firebase if it hasn't been already
        if(FirebaseApp.getApps(this).isEmpty()){
            FirebaseApp.initializeApp(this);
        }

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

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

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(
                this,
                task -> {
                    // Hide progress bar
                    progressBar.setVisibility(View.GONE);


                    // Check : If login successful
                    if(task.isSuccessful()){
                        Toast.makeText(
                                LoginRegisterActivity.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                        ).show();

                        Intent intent = new Intent(LoginRegisterActivity.this, ChatbotActivity.class);
                        startActivity(intent);
                        finish();

                    } else{

                        Toast.makeText(
                                LoginRegisterActivity.this,
                                "Login failed: " +  Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_LONG
                        ).show();

                    }


                }
        );




        // TODO: Handle login logic
        // ...
        // If login is successful:
        Intent intent = new Intent(LoginRegisterActivity.this, ChatbotActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerOnClickEvent(){

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check : If username field empty
        if(TextUtils.isEmpty(username)){
            editTextUsername.setError("Username is required.");
            return;
        }

        // Check : If password field empty
        if(TextUtils.isEmpty(password)){
            editTextPassword.setError("Password is required.");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(
                    this,
                    task -> {
                        // Hide progressbar
                        progressBar.setVisibility(View.GONE);

                        // Check : If Registration is successful
                        if(task.isSuccessful()){
                            Toast.makeText(LoginRegisterActivity.this,
                                    "Registration is successful",
                                    Toast.LENGTH_SHORT
                            ).show();


                            Intent intent = new Intent(LoginRegisterActivity.this, ChatbotActivity.class);
                            startActivity(intent);
                            finish();


                        } else {

                            Toast.makeText(
                                    LoginRegisterActivity.this,
                                    "Registration Failed.",
                                    Toast.LENGTH_LONG
                            ).show();

                        }





                    }
                );


    }

    // TODO: Implement Methods for handling login and registration
}
