package com.example.echatbotproject.concretes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Importing base activity class
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import android.view.Menu;
import android.view.MenuItem;

import com.example.echatbotproject.BuildConfig;
import com.example.echatbotproject.R;
import com.example.echatbotproject.abstracts.interfaces.ModelResponseCallback;
import com.example.echatbotproject.concretes.chatting.ChatAdapter;
import com.example.echatbotproject.concretes.chatting.ChatMessage;
import com.example.echatbotproject.concretes.genai.GeminiHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;



public class ChatbotActivity extends AppCompatActivity implements ModelResponseCallback {

    // Properties
    private final String TAG = "CHATBOTACTIVITY";
    private final String API_KEY = BuildConfig.API_KEY;
    private final String MODEL_NAME = BuildConfig.MODEL_NAME;
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private GeminiHelper geminiHelper;


    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();



        // TODO: Load Chat History
        //loadHistory();
    }

    // Private Methods

    private void init(){
        initFrontend();
        initBackend();
        initEventListeners();
    }

    private void initBackend(){
        // Initialize Backend elements and connect them with frontend
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        geminiHelper = new GeminiHelper(API_KEY, MODEL_NAME, this);
    }

    private void initFrontend(){
        // Initialize UI elements
        setContentView(R.layout.activity_chatbot);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
    }

    private void initEventListeners(){
        // Initialize Event Listeners
        buttonSend.setOnClickListener(v -> {
            sendOnClickEvent();
        });
    }


    private void addMessage(String message, boolean isUser){
        chatMessages.add(new ChatMessage(message, isUser));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
    }

    private void loadHistory(){
        // TODO: Implement loading chat history from local device
    }


    // Overriden Methods

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

    @Override // Add Model response to chat
    public void onSuccess(String responseMessage) {
        runOnUiThread(() -> {
            // This code will now execute on the main UI thread
            addMessage(responseMessage, false);
        });
    }

    @Override // Add Error to chat and log it out for debugging
    public void onError(String errorMessage) {
        Snackbar.make(recyclerViewChat, errorMessage, Snackbar.LENGTH_LONG).show();
        Log.e(TAG, errorMessage);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    // Event Listeners
    private void sendOnClickEvent(){
        String userMessage = editTextMessage.getText().toString().trim();
        if(!TextUtils.isEmpty(userMessage)){
            addMessage(userMessage, true);
            editTextMessage.setText("");
            geminiHelper.generateResponse(userMessage);
        }
    }


}
