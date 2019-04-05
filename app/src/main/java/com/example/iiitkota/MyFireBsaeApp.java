package com.example.iiitkota;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyFireBsaeApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
