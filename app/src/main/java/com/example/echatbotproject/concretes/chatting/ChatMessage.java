package com.example.echatbotproject.concretes.chatting;

import java.util.Date;

public class ChatMessage {

    // Properties
    private String message;
    private boolean isUser;
    private long timestamp; // Add this

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
        this.timestamp = new Date().getTime(); // Initialize with the current time
    }

    // Getter Setter
    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}