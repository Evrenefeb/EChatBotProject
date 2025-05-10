package com.example.echatbotproject.concretes.app;

import android.app.Application;

// Importing Firebase App
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    // MAIN APPLICATION

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
