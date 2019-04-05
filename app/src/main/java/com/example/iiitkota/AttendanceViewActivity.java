package com.example.iiitkota;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceViewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    StudentAdapter adapter;
    ArrayList<String> headerList = new ArrayList<>();
    HashMap<String, ArrayList<Pair<String, String>>> childList = new HashMap<>();
    Intent intent;
    FirebaseDatabase mRef;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ExpandableListView listView = findViewById(R.id.expstu);

        adapter = new StudentAdapter(AttendanceViewActivity.this,headerList,childList);

        listView.setAnimation(new AlphaAnimation((float) 0.2, (float) 1.0));

        listView.setAdapter(adapter);

        intent = getIntent();

        String subject, databaseReferance, show;

        subject = intent.getStringExtra("Subject");

        databaseReferance = intent.getStringExtra("Database Referance key");

        show = intent.getStringExtra("Show");

        mRef = FirebaseDatabase.getInstance();

        ref = mRef.getReference(databaseReferance);


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Getting the child from database and parsing it to list class object
                List list = dataSnapshot.getValue(List.class);

                // Setting up the key for the data in the list object
                list.setKey(dataSnapshot.getKey());

                // Adding list object to the ArrayList of dataset

                headerList.add(list.getStudent_ID());

                ArrayList<Pair<String,String>> str = new ArrayList<>();
                if (show.equals("Attendance")) {

                    for (HashMap.Entry<String, String> it : list.getAttendance().get(subject).entrySet()) {
                        str.add(new Pair<>(it.getKey(),it.getValue()));
                    }
                    childList.put(list.getStudent_ID(),str);
                } else if (show.equals("Marks")) {
                    for (HashMap.Entry<String, String> it : list.getMarks().get(subject).entrySet()) {
                        str.add(new Pair<>(it.getKey(),it.getValue()));
                    }
                    childList.put(list.getStudent_ID(),str);
                }
                /*Adding dataSet change callback function to Adapter*/
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(AttendanceViewActivity.this,SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.attendance) {

            //Creating an Intent to Attendance activity
            Intent in = new Intent(AttendanceViewActivity.this, Attendance.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            //Launching intent
            startActivity(in);

        } else if (id == R.id.marks) {

            //Creating an Intent to Attendance activity
            Intent in = new Intent(AttendanceViewActivity.this, Marks.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            //Launching intent
            startActivity(in);

        } else if (id == R.id.sigot) {

            //Signing out the current user
            FirebaseAuth.getInstance().signOut();

            //Launching login activity
            startActivity(new Intent(AttendanceViewActivity.this, MainActivity.class));

            //finishing this activity
            finish();
        } else if (id == R.id.ext) {

            //Exiting the application
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else if (id == R.id.attendancevi) {
            //Creating an Intent to Attendance activity
            Intent in = new Intent(AttendanceViewActivity.this, AttendanceViewActivity.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            in.putExtra("Show","Attendance");

            //Launching intent
            startActivity(in);
        } else if (id == R.id.marksv) {
            //Creating an Intent to Attendance activity
            Intent in = new Intent(AttendanceViewActivity.this, AttendanceViewActivity.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            in.putExtra("Show","Marks");

            //Launching intent
            startActivity(in);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
