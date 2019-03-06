package com.example.iiitkota;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Using screen Handler to give thread a pause for splash screen to show
        //Declaring the time length of splash screen
        int SPLASH_LENGTH = 1000;
        new Handler().postDelayed(() -> {
            // Declaring and implementing Intent to main activity
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            //Finishing this activity
            finish();
        }, SPLASH_LENGTH);
    }
}
