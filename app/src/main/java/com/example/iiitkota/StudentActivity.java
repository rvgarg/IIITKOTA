package com.example.iiitkota;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class StudentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference mRef;
    List data;
    ArrayList<String> headerList = new ArrayList<>();
    HashMap<String, ArrayList<Pair<String, String>>> childListAttendance = new HashMap<>();
    HashMap<String, ArrayList<Pair<String, String>>> childListMarks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ProgressDialog p = new ProgressDialog(this);
        p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        p.setMessage("Loading Data!!");
        p.setCancelable(false);
        p.show();

        StudentAdapter attendanceAdapter = new StudentAdapter(StudentActivity.this, headerList, childListAttendance);
        StudentAdapter marksAdapter = new StudentAdapter(StudentActivity.this, headerList, childListMarks);

        database = FirebaseDatabase.getInstance();

        String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        id = id.substring(0, id.indexOf('@'));

        String ref = id.substring(0, id.indexOf('k')) + id.substring(6, 8);

        id = "K" + id.toUpperCase();

        mRef = database.getReference(ref);
        mRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(List.class);
                data.setKey(dataSnapshot.getKey());
                prepHeaderList();
                prepChildListAttendance();
                prepChildListMarks();
                marksAdapter.notifyDataSetChanged();
                attendanceAdapter.notifyDataSetChanged();
                p.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ExpandableListView Attendance = findViewById(R.id.attendance);
        ExpandableListView Marks = findViewById(R.id.marks);


        Attendance.setAdapter(attendanceAdapter);
        Marks.setAdapter(marksAdapter);

        View header = navigationView.getHeaderView(0);

        TextView nam = header.findViewById(R.id.nam);
        TextView tid = header.findViewById(R.id.id);

        nam.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " ");
        tid.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.sig) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(StudentActivity.this, MainActivity.class));
            finish();
        } else if (id == R.id.ex) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void prepHeaderList() {
        for (HashMap.Entry<String, HashMap<String, String>> ht : data.getAttendance().entrySet()) {
            headerList.add(ht.getKey());
        }

    }

    public void prepChildListAttendance() {
        HashMap<String, HashMap<String, String>> dat;
        dat = data.getAttendance();
        for (HashMap.Entry<String, HashMap<String, String>> it : dat.entrySet()) {
            ArrayList<Pair<String, String>> str = new ArrayList<>();
            for (HashMap.Entry<String, String> ht : it.getValue().entrySet()) {
                str.add(new Pair<>(ht.getKey(), ht.getValue()));
            }
            childListAttendance.put(it.getKey(), str);
        }
    }

    public void prepChildListMarks() {
        HashMap<String, HashMap<String, String>> dat;
        dat = data.getAttendance();
        for (HashMap.Entry<String, HashMap<String, String>> it : dat.entrySet()) {
            ArrayList<Pair<String, String>> str = new ArrayList<>();
            for (HashMap.Entry<String, String> ht : it.getValue().entrySet()) {
                str.add(new Pair<>(ht.getKey(), ht.getValue()));
            }
            childListMarks.put(it.getKey(), str);
        }
    }
}

