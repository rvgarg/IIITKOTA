package com.example.iiitkota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<List> listStudents = new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //Taking up referance of toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing intent to get the information brought to the activity by the intent that triggered this activity
        intent = getIntent();

        //Declaring recycler view for students list
        RecyclerView recyclerView = findViewById(R.id.recycler);

        //Declaring and initializing the adapter for recycler view
        MyAdapter adapter = new MyAdapter(listStudents, intent.getStringExtra("Subject"), intent.getStringExtra("Database Referance key"));

        //Declaring layout manager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //Setting up layout manager to the recycler view
        recyclerView.setLayoutManager(layoutManager);

        //Setting up adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //Setting up recycler view animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        /*Making an object of drawer layout */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //Declaring appbar toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Adding toggle listener to the drawer and syncing its state
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Taking referance of the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);

        ProgressDialog p = new ProgressDialog(this);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setMessage("Loading Data!!");
        p.setCancelable(false);
        p.show();

        //Setting up navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView nam = header.findViewById(R.id.nam);
        TextView tid = header.findViewById(R.id.id);

        nam.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " ");
        tid.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        /*Getting database referance key*/
        DatabaseReference myRef = database.getReference(intent.getStringExtra("Database Referance key"));

        //Adding child event listener to this database referance
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // Getting the child from database and parsing it to list class object
                List list = dataSnapshot.getValue(List.class);

                // Setting up the key for the data in the list object
                list.setKey(dataSnapshot.getKey());

                // Adding list object to the ArrayList of dataset
                listStudents.add(list);

                /*Adding dataSet change callback function to Adapter*/
                adapter.notifyDataSetChanged();

                if (p.isShowing()) {
                    p.dismiss();
                }
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

        Button submit = findViewById(R.id.save);
        submit.setOnClickListener(v -> {
            int total = adapter.getTotalAttendance();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Total Attendance is " + total);
            builder.setPositiveButton("Ok", (dialog, which) -> {
                startActivity(new Intent(Attendance.this, LoggedIn.class));
                finish();
            });
            adapter.notifySavePressed();
            builder.show();
        });
    }

    // Overriding back button pressed listener function
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
            startActivity(new Intent(Attendance.this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.attendance) {

        } else if (id == R.id.marks) {

            //Creating an Intent to Attendance activity
            Intent in = new Intent(Attendance.this, Marks.class);

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
            startActivity(new Intent(Attendance.this, MainActivity.class));

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
            Intent in = new Intent(Attendance.this, AttendanceViewActivity.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            in.putExtra("Show", "Attendance");

            //Launching intent
            startActivity(in);
        } else if (id == R.id.marksv) {
            //Creating an Intent to Attendance activity
            Intent in = new Intent(Attendance.this, AttendanceViewActivity.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            in.putExtra("Show", "Marks");

            //Launching intent
            startActivity(in);
        }

        //Setting up drawer layout default and compulsory response
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
