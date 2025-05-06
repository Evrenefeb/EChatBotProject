package com.example.echatbotproject.concretes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.echatbotproject.BuildConfig;
import com.example.echatbotproject.R;
import com.example.echatbotproject.abstracts.interfaces.ModelResponseCallback;
import com.example.echatbotproject.concretes.chatting.ChatAdapter;
import com.example.echatbotproject.concretes.chatting.ChatMessage;
import com.example.echatbotproject.concretes.genai.GeminiHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static final String CHAT_HISTORY_KEY = "chatHistory";

    // Overriden Methods

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        gson = new Gson();
        loadHistory(); // Load history on create
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean clearHistoryNow = sharedPreferences.getBoolean("clearHistoryNow", false);
        if (clearHistoryNow) {
            chatMessages.clear();
            saveHistory(); // Save the cleared history
            chatAdapter.notifyDataSetChanged();
            sharedPreferences.edit().putBoolean("clearHistoryNow", false).apply(); // Reset the flag
        }
        checkForAutoClearHistory(); // Check for auto-clear on resume
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveHistory(); // Save history when activity is paused
    }

    // Private Methods

    private void init() {
        initFrontend();
        initBackend();
        initEventListeners();
    }

    private void initBackend() {
        // Initialize Backend elements and connect them with frontend
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        geminiHelper = new GeminiHelper(API_KEY, MODEL_NAME, this);
    }

    private void initFrontend() {
        // Initialize UI elements
        setContentView(R.layout.activity_chatbot);
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
    }

    private void initEventListeners() {
        // Initialize Event Listeners
        buttonSend.setOnClickListener(v -> {
            sendOnClickEvent();
        });
    }

    private void addMessage(String message, boolean isUser) {
        chatMessages.add(new ChatMessage(message, isUser));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
        saveHistory(); // Save history after adding a message
    }

    private void loadHistory() {
        String json = sharedPreferences.getString(CHAT_HISTORY_KEY, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<ChatMessage>>() {}.getType();
            chatMessages = gson.fromJson(json, listType);
            if (chatMessages == null) {
                chatMessages = new ArrayList<>();
            }
        } else {
            chatMessages = new ArrayList<>();
        }

        // TEMPORARY CODE FOR TESTING AUTO-CLEAR (REMOVE LATER!)
        if (chatMessages.isEmpty()) {
            long thirtyOneDaysAgo = new Date().getTime() - TimeUnit.DAYS.toMillis(31);
            long sixtyOneDaysAgo = new Date().getTime() - TimeUnit.DAYS.toMillis(61);
            chatMessages.add(new ChatMessage("Old message 1 (31 days)", false));
            chatMessages.get(0).setTimestamp(thirtyOneDaysAgo);
            chatMessages.add(new ChatMessage("Old message 2 (61 days)", true));
            chatMessages.get(1).setTimestamp(sixtyOneDaysAgo);
            chatMessages.add(new ChatMessage("New message", false));
        }
        // END OF TEMPORARY CODE
    }

    private void saveHistory() {
        String json = gson.toJson(chatMessages);
        sharedPreferences.edit().putString(CHAT_HISTORY_KEY, json).apply();
    }

    private void checkForAutoClearHistory() {
        String interval = sharedPreferences.getString("clearHistoryInterval", "never");
        long cutoffTime = 0;

        long currentTime = new Date().getTime();

        switch (interval) {
            case "30":
                cutoffTime = currentTime - TimeUnit.DAYS.toMillis(30);
                break;
            case "60":
                cutoffTime = currentTime - TimeUnit.DAYS.toMillis(60);
                break;
            case "90":
                cutoffTime = currentTime - TimeUnit.DAYS.toMillis(90);
                break;
            case "never":
            default:
                return; // Do not clear if set to never
        }

        List<ChatMessage> newMessages = new ArrayList<>();
        for (ChatMessage message : chatMessages) {
            if (message.getTimestamp() >= cutoffTime) {
                newMessages.add(message);
            }
        }

        if (newMessages.size() < chatMessages.size()) {
            chatMessages.clear();
            chatMessages.addAll(newMessages);
            saveHistory();
            chatAdapter.notifyDataSetChanged();
            Log.i(TAG, "Chat history automatically cleared (older than " + interval + " days).");
        }
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

        if (id == R.id.action_settings) {
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
    public void onDestroy() {
        super.onDestroy();
    }

    // Event Listeners
    private void sendOnClickEvent() {
        String userMessage = editTextMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(userMessage)) {
            addMessage(userMessage, true);
            editTextMessage.setText("");
            geminiHelper.generateResponse(userMessage);
        }
    }
}