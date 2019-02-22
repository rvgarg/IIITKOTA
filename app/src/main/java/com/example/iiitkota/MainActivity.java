package com.example.iiitkota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText user, pwd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Edittexts
        user = findViewById(R.id.userid);
        pwd = findViewById(R.id.Password);
        //Initializing forgot password Textview
        TextView frgt = findViewById(R.id.btnforgotpassword);
        Button lgin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        lgin.setOnClickListener(v -> {
            String email, password;
            email = user.getText().toString();
            password = pwd.getText().toString();
            if (email.equals(null) || password.equals(null)) {
                Toast.makeText(MainActivity.this, "Enter email id and password", Toast.LENGTH_LONG).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        });
            }
        });
        frgt.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, forgotPassword.class);
            startActivity(intent);
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(MainActivity.this, LoggedIn.class);
        startActivity(intent);
    }
}
