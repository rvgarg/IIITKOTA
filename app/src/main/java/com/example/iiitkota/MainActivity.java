package com.example.iiitkota;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

//        Initializing login Button
        Button lgin = findViewById(R.id.btnLogin);

        //Getting authentication instance
        mAuth = FirebaseAuth.getInstance();

        //Setting up on click listener on the login button
        lgin.setOnClickListener(v -> {

            //Declaring the variables for email address and password
            String email, password;

            //Fetching the user inputted email address and password
            email = user.getText().toString().trim();
            password = pwd.getText().toString().trim();

            //Checking if the email address or password is empty
            if (email.isEmpty() || password.isEmpty()) {

                //Showing message to hte user
                Toast.makeText(MainActivity.this, "Enter email id and password", Toast.LENGTH_LONG).show();
            } else {

                //Sending request for the verification email address and password
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                //Getting the current user information
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Updating the activity according to the user
                                updateUI(user, task.getResult().getAdditionalUserInfo().isNewUser());
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        //Applying on click listener to the forgot password textview
        frgt.setOnClickListener(v -> {

            //Changing activity to the forgot password activity
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
            updateUI(currentUser, false);
    }

    private void updateUI(FirebaseUser currentUser, boolean bool) {
//    if(Long.parseLong(String.valueOf(currentUser.getMetadata().getLastSignInTimestamp())) == NULL)
        //Checking if the user is new or not
        if (bool) {

            //Declaring 3 linear layout for building alert box to change password for time users
            LinearLayout layout = new LinearLayout(this);
            LinearLayout lpass = new LinearLayout(this);
            LinearLayout lcnf = new LinearLayout(this);

            //Declaring Alert dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //Declaring EditTexts and TextViews for the alert dialog box
            final EditText pass = new EditText(this);
            final EditText cnf = new EditText(this);
            final TextView tpass = new TextView(this);
            final TextView tcnf = new TextView(this);

            //setting linear layouts orientations
            lpass.setOrientation(LinearLayout.HORIZONTAL);
            lcnf.setOrientation(LinearLayout.HORIZONTAL);
            layout.setOrientation(LinearLayout.VERTICAL);

            //Setting input types on EditTexts
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            cnf.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

            //Setting text to the TextViews
            tpass.setText("Enter Password");
            tcnf.setText("Confirm Password");

            //Adding TextViews and EditTexts to the following LinearLayouts
            lpass.addView(tpass);
            lpass.addView(pass);
            lcnf.addView(tcnf);
            lcnf.addView(cnf);

            //Adding LinearLayouts to the root layout
            layout.addView(lpass);
            layout.addView(lcnf);

            //Setting root layout to AlertDialog
            builder.setView(layout);

            //Adding the ok or positive button to the AlertDialog
            builder.setPositiveButton("Ok", (dialog, which) -> {
                String Pass = pass.getText().toString().trim(), Cnf = cnf.getText().toString().trim();

                //Checking if both the passwords entered matches or not
                if (Pass.equals(Cnf)) {
                    currentUser.updatePassword(Pass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    //Changing Activity after password is changed
                                    Intent intent = new Intent(MainActivity.this, LoggedIn.class);
                                    startActivity(intent);
                                }
                            });
                } else {

                    //Message to the user when passwords entered doesn't match
                    Toast.makeText(this, "Password didnt match", Toast.LENGTH_LONG).show();
                }
            });
        } else {

            //Changing activity when user is old
            Intent intent = new Intent(MainActivity.this, LoggedIn.class);
            startActivity(intent);
        }
    }
}
