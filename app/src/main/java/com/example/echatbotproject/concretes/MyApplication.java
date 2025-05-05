package com.example.echatbotproject.concretes;

import android.app.Application;

// Importing Firebase App
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
