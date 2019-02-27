package com.example.iiitkota;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    DrawerLayout drawer;
    NavigationView nav;
    ArrayList<List> stuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        //Initializing NavigationView
        nav = findViewById(R.id.nav_view1);

        //Initializing DrawerLayout
        drawer = findViewById(R.id.drawerMarks);

        //Setting navigation item select listener
        nav.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.sigot:
                    //Signing out the user
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Attendance.this, MainActivity.class));
                    break;

                case R.id.ext:
//                   Exiting the application
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    break;

                case R.id.marks:
                    break;

                case R.id.attend:
                    break;

            }
            drawer.closeDrawers();
            return true;
        });

        //Initializing recycler view
        recyclerView = findViewById(R.id.recycler);

//Initializing layout manager
        layoutManager = new LinearLayoutManager(this);

        //Getting intent
        Intent intent = getIntent();

        //Getting extra data from intent
        String referance = new String();
        intent.getStringExtra(referance);

        //Getting reference of the firebase database
        myRef = database.getReference().child(referance);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stuList.add(dataSnapshot.getValue(List.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MyAdapter myAdapter = new MyAdapter(stuList);

        //Setting up recycler view
        //Setting Adapter
        recyclerView.setAdapter(myAdapter);
        //Setting fixed size attribute
        recyclerView.setHasFixedSize(true);
        //Setting layout manager
        recyclerView.setLayoutManager(layoutManager);
    }

}
