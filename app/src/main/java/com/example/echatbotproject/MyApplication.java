package com.example.echatbotproject;

import android.app.Application;

// Importing Firebase App
import com.example.echatbotproject.genai.GeminiHelper;
import com.google.firebase.FirebaseApp;

import java.io.IOException;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
