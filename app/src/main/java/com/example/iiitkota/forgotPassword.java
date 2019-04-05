package com.example.iiitkota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
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

        ProgressDialog p = new ProgressDialog(this);
        p.setCancelable(false);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setMessage("Sending Email!!");

        //Getting referance of submit button from the layout file
        MaterialButton submit = findViewById(R.id.subt);

        //Getting referance of email edit text
        email = findViewById(R.id.email);

        //Setting on click listener on submit button
        submit.setOnClickListener(v -> {

            p.show();
            //Getting email address entered by the user
            emailAddress = email.getText().toString();

            //Checking for null email address
            if (!TextUtils.isEmpty(emailAddress)) {

                //Sending password reset link
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                p.dismiss();
                                Toast.makeText(forgotPassword.this, "Email Sent!!!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(forgotPassword.this,MainActivity.class));
                                finish();
                            } else {
                                p.dismiss();
                                Toast.makeText(forgotPassword.this, "Failed to send email!!!", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                p.dismiss();
                Toast.makeText(forgotPassword.this, "Enter registered Email Address !!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
