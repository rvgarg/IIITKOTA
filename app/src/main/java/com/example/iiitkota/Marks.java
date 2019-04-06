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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Marks extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<List> listStudents = new ArrayList<>();
    private Intent intent;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        //Declaring and initializing toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        //Setting up support action toolbar
        setSupportActionBar(toolbar);

        //Getting hte intent that triggered this activity
        intent = getIntent();

        ProgressDialog p = new ProgressDialog(this);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setMessage("Loading Data!!");
        p.setCancelable(false);
        p.show();

        user = mAuth.getCurrentUser();

        //Declaring and initializing recycler view
        RecyclerView recyclerView = findViewById(R.id.recycler);

        //Declaring and initializing adapter for recycler layout
        MarksAdapter adapter = new MarksAdapter(listStudents, intent.getStringExtra("Subject"), intent.getStringExtra("Database Referance key"));

        //Declaring and initializing layout manager for recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //Setting layout manager for recycler view
        recyclerView.setLayoutManager(layoutManager);

        //Setting adapter for recycler view
        recyclerView.setAdapter(adapter);

        //Setting animator for recycler view
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Declaring and initializing drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);

        //Declaring and initializing drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Adding app drawer toggle listener syncing its state
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Declaring and initializing the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view1);

        //Setting navigation item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView nam = header.findViewById(R.id.nam);
        TextView tid = header.findViewById(R.id.id);
        ImageView imageView = header.findViewById(R.id.imageView);

        if (user.getPhotoUrl() != null)
            Glide.with(this).load(user.getPhotoUrl()).placeholder(R.drawable.ic_person_recycle_24dp).into(imageView);

        nam.setText(user.getDisplayName() + " ");
        tid.setText(user.getEmail());

        //Getting database referance
        DatabaseReference myRef = database.getReference(intent.getStringExtra("Database Referance key"));

        //Adding child event listener to the database referance
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //Storing the child in a list object
                List list = dataSnapshot.getValue(List.class);

                //Adding the key of the child
                list.setKey(dataSnapshot.getKey());

                // Adding child to data set
                listStudents.add(list);

                /*Adding dataSet change callback function to Adapter */
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
            adapter.notifySavePressed();
            startActivity(new Intent(Marks.this, LoggedIn.class));
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
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
            startActivity(new Intent(Marks.this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.attendance) {
            Intent inte = new Intent(Marks.this, Attendance.class);

            //Adding database referance key to the intent as extra information
            inte.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            inte.putExtra("Subject", intent.getStringExtra("Subject"));

            //Launching this intent
            startActivity(inte);

        } else if (id == R.id.marks) {

        } else if (id == R.id.sigot) {

            //Signing out the current user
            FirebaseAuth.getInstance().signOut();

            //Launching login activity
            startActivity(new Intent(Marks.this, MainActivity.class));

            //Finishing this activity
            finish();
        } else if (id == R.id.ext) {

            //Exiting the application
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else if (id == R.id.attendancevi) {
            //Creating an Intent to Attendance activity
            Intent in = new Intent(Marks.this, AttendanceViewActivity.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            in.putExtra("Show", "Attendance");

            //Launching intent
            startActivity(in);
        } else if (id == R.id.marksv) {
            //Creating an Intent to Attendance activity
            Intent in = new Intent(Marks.this, AttendanceViewActivity.class);

            //Adding database referance key to the intent as extra information
            in.putExtra("Database Referance key", intent.getStringExtra("Database Referance key"));

            //Adding subject to be accessed in database to intent
            in.putExtra("Subject", intent.getStringExtra("Subject"));

            in.putExtra("Show", "Marks");

            //Launching intent
            startActivity(in);
        }

        //Drawer layout default functionality
        DrawerLayout drawer = findViewById(R.id.drawer_layout1);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
