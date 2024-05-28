package com.example.firebasetest;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
