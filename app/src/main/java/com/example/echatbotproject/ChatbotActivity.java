package com.example.echatbotproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Importing base activity class
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.view.Menu;
import android.view.MenuItem;

public class ChatbotActivity extends AppCompatActivity{

    // Properties
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;


    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatbot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent intent = new Intent(ChatbotActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
