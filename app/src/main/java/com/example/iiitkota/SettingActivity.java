package com.example.iiitkota;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SettingActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private final int REQUEST_IMAGE_GET = 1;
    private ImageView profilePic;
    private ImageView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(user.getUid() + "/profilePicture");

        profilePic = findViewById(R.id.img);


        edit = findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_GET);
            }
        });


        if (user.getPhotoUrl() != null)
            Glide.with(this).load(user.getPhotoUrl()).placeholder(R.drawable.ic_person_recycle_24dp).into(profilePic);


        TextInputEditText mDisplayName, mMobileNumber, nPass, cPass;
        mDisplayName = findViewById(R.id.dispName);
        mMobileNumber = findViewById(R.id.mobNumber);
        nPass = findViewById(R.id.newPass);
        cPass = findViewById(R.id.newPassre);

        Button submit = findViewById(R.id.su);

        mDisplayName.setText(user.getDisplayName());
        mMobileNumber.setText(user.getPhoneNumber());


        Button changePassword = findViewById(R.id.passChange);
        Button save = findViewById(R.id.sav);


        //Setting up on click listener on change password button
        changePassword.setOnClickListener(v -> {
            //Declaring and initializing textinputlayouts
            TextInputLayout lnPass, lcPass;
            lnPass = findViewById(R.id.lnewPass);
            lcPass = findViewById(R.id.lnewPassre);


            lnPass.setVisibility(View.VISIBLE);
            lcPass.setVisibility(View.VISIBLE);
            nPass.setVisibility(View.VISIBLE);
            cPass.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            save.setVisibility(View.GONE);
        });

        submit.setOnClickListener(v -> {
            String np, cp;
            np = nPass.getText().toString();
            cp = cPass.getText().toString();

            if (TextUtils.isEmpty(np) || TextUtils.isEmpty(cp)) {
                Toast.makeText(this, "Enter the new password", Toast.LENGTH_LONG).show();
            } else if (!np.equals(cp)) {
                Toast.makeText(this, "Entered passwords doesn't match", Toast.LENGTH_LONG).show();
            } else {
                user.updatePassword(np).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SettingActivity.this, "Password updated !!!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SettingActivity.this, "Failed to update password!!!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        save.setOnClickListener(v -> {
            String name = mDisplayName.getText().toString();
            String number = mMobileNumber.getText().toString();
            UserProfileChangeRequest profileUpdates;
            if (!TextUtils.isEmpty(name)) {
                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
            } else {
                profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName("")
                        .build();
            }
            if (!TextUtils.isEmpty(number)) {
                PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(SettingActivity.this, "Failed to verify Mobile number!!!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        Toast.makeText(SettingActivity.this, "Verification code sent!!", Toast.LENGTH_LONG).show();
                        AlertDialog dia = new AlertDialog.Builder(SettingActivity.this).setView(R.layout.code_pick).create();
                        dia.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", (dialog, which) -> {
                            String Code = ((TextInputEditText) dia.findViewById(R.id.code)).getText().toString();
                            PhoneAuthCredential cre = PhoneAuthProvider.getCredential(s, Code);
                            FirebaseAuth.getInstance().signInWithCredential(cre).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingActivity.this, "Mobile number verified", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SettingActivity.this, "Failed to verify Mobile number!!!", Toast.LENGTH_LONG).show();
                                }
                            });
                        });
                        dia.show();
                    }
                };
                PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 120, TimeUnit.SECONDS, SettingActivity.this, mCallbacks);
            }

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Profile updated!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "Could not update profile!! try again later", Toast.LENGTH_LONG).show();
                        }
                    });

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fullPhotoUri = data.getData();
            Bitmap thumbnail;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getContentResolver(), fullPhotoUri);
                profilePic.setImageBitmap(thumbnail);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fullPhotoUri.getLastPathSegment() != null)
                storageReference = storageReference.child(fullPhotoUri.getLastPathSegment());
            storageReference.putFile(fullPhotoUri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    if (task.getException() != null)
                        throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    if (downloadUri == null) {
                        return;
                    }

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                    user.updateProfile(profileChangeRequest).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Toast.makeText(SettingActivity.this, "Profile picture uploaded!!!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SettingActivity.this, "Failed to update profile picture!!!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(e -> Toast.makeText(SettingActivity.this, "Failed to update profile picture!!!", Toast.LENGTH_LONG).show());
        }
    }

}
