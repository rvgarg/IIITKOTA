package com.example.iiitkota;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String emailAddress;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Button submit = findViewById(R.id.subt);
        email = findViewById(R.id.email);

        submit.setOnClickListener(v -> {
            emailAddress = email.getText().toString();

            //Checking for null email address
            if (!TextUtils.isEmpty(emailAddress)) {
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgotPassword.this, "Email Sent!!!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(forgotPassword.this, "Failed to send email!!!", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(forgotPassword.this, "Enter registered Email Address !!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
