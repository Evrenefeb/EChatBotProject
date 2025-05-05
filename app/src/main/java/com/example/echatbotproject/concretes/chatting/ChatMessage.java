package com.example.echatbotproject.concretes.chatting;

public class ChatMessage {

    // Properties
    private String text;
    private boolean isUser;

    // Constructors
    public ChatMessage(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    // Getter Setter
    public String getText(){
        return text;
    }
    public boolean isUser(){
        return isUser;
    }
}
