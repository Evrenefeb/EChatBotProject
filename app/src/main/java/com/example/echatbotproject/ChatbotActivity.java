package com.example.echatbotproject;

import androidx.annotation.Nullable;

// Importing base activity class
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class ChatbotActivity extends AppCompatActivity{

    // Properties
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;


    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize UI elements
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // Initialize Event Listeners
        buttonSend.setOnClickListener(v -> {
            // TODO: Handle sending message logic
        });

        // TODO: Set up RecyclerView

        // TODO: Load Chat History

    }
}
