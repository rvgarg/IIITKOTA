package com.example.iiitkota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class MainActivity extends AppCompatActivity {
    private EditText user, pwd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog p = new ProgressDialog(this);
        p.setCancelable(false);
        p.setMessage("Signing In");
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Initializing Edittexts
        user = findViewById(R.id.userid);
        pwd = findViewById(R.id.Password);

        //Initializing forgot password Textview
        TextView frgt = findViewById(R.id.btnforgotpassword);

//        Initializing login Button
        Button lgin = findViewById(R.id.btnLogin);

        //Getting authentication instance
        mAuth = getInstance();

        //Setting up on click listener on the login button
        lgin.setOnClickListener(v -> {

            p.show();

            //Declaring the variables for email address and password
            String email, password;

            //Fetching the user inputted email address and password
            email = user.getText().toString().trim();
            password = pwd.getText().toString().trim();

            //Checking if the email address or password is empty
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

                p.dismiss();

                //Showing message to hte user
                Toast.makeText(MainActivity.this, "Enter email id and password", Toast.LENGTH_LONG).show();
            } else {

                //Sending request for the verification email address and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {

                                p.dismiss();
                                //Getting the current user information
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Updating the activity according to the user
                                updateUI(user);
                            } else {
                                p.dismiss();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Applying on click listener to the forgot password textview
        frgt.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_forgot_password);
            dialog.setTitle("Forgot Password?");
            dialog.findViewById(R.id.subt).setOnClickListener(v1 -> {
                p.setMessage("Sending Email!!");
                p.show();
                EditText em = dialog.findViewById(R.id.email);
                String eMail = em.getText().toString().trim();
                if (!TextUtils.isEmpty(eMail)) {

                    //Sending password reset link
                    mAuth.sendPasswordResetEmail(eMail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    p.dismiss();
                                    Toast.makeText(MainActivity.this, "Email Sent!!!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    p.dismiss();
                                    Toast.makeText(MainActivity.this, "Failed to send email!!!", Toast.LENGTH_LONG).show();
                                }
                            });
                } else {
                    p.dismiss();
                    Toast.makeText(MainActivity.this, "Enter registered Email Address !!", Toast.LENGTH_LONG).show();
                }

            });
            dialog.show();
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
//    if(Long.parseLong(String.valueOf(currentUser.getMetadata().getLastSignInTimestamp())) == NULL)
        if (currentUser != null)
            if (!currentUser.getEmail().contains("20")) {
                //Changing activity when user is old
                Intent intent = new Intent(MainActivity.this, LoggedIn.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent);
                finish();
            }

    }
}
