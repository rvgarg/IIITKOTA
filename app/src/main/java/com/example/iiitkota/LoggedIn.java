package com.example.iiitkota;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoggedIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Spinner year;
    private Spinner subject;
    private Spinner section;
    private String access;
    private NavigationView nav_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        //Initializing the drawer layout
        drawerLayout = findViewById(R.id.drawer);

        //Getting firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //Initializing the navigation view
        nav_View = findViewById(R.id.nav_view);

        View header = nav_View.getHeaderView(0);

        TextView nam = header.findViewById(R.id.nam);
        TextView tid = header.findViewById(R.id.id);

        nam.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + " ");
        tid.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        //Setting up the listener on navigation view
        nav_View.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.logout:

                    //Signing out the user
                    mAuth.signOut();

                    //Launching login activity
                    startActivity(new Intent(LoggedIn.this, MainActivity.class));

                    //finishing this activity
                    finish();
                    break;
                case R.id.exit:
//                   Exiting the application
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        });

        //Initialization of spinners
        year = findViewById(R.id.year);
        section = findViewById(R.id.section);
        subject = findViewById(R.id.subject);

        //Setting up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //Creating arrayadapters for the spinners
        ArrayAdapter<CharSequence> Years;

        //creating array adapters
        Years = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);

        //Specifing Layouts for spinners
        Years.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Applying adapters to spinners
        year.setAdapter(Years);

        //Setting on click listener on years spinner
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                           @Override
                                           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                               if (year.getSelectedItemPosition() != 0) {

                                                   //Declaring and initializing array adapter for spinner
                                                   ArrayAdapter<CharSequence> Sections = ArrayAdapter.createFromResource(LoggedIn.this, R.array.section, android.R.layout.simple_spinner_item);

                                                   //Setting up layout for dropdown style
                                                   Sections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                   //Setting up adapter for sections spinner
                                                   section.setAdapter(Sections);

                                                   //Setting subjects spinner adapter to null
                                                   subject.setAdapter(null);

                                                   //Calling function for choosing subject list
                                                   subChoser();
                                               }
                                           }

                                           @Override
                                           public void onNothingSelected(AdapterView<?> parent) {
//                                             Toast.makeText(LoggedIn.this, "Select a year", Toast.LENGTH_LONG).show();
                                           }
                                       }
        );

        //Applying item selection listeners
        section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Calling function to choose subject list
                subChoser();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Initializing and setting on click listener on the submit button
        Button enterAttandance = findViewById(R.id.enterAttandace);
        enterAttandance.setOnClickListener(v -> {
            if (subject.getSelectedItemPosition() != 0) {

                //Declaring and initializing intent for attendance activity
                Intent intent = new Intent(LoggedIn.this, Attendance.class);

                //Adding database referance key name to the intent
                intent.putExtra("Database Referance key", access);

                //Adding the subject chosen by the user
                intent.putExtra("Subject", subject.getSelectedItem().toString());

                //Launching intent
                startActivity(intent);

            } else {
                Toast.makeText(this, "Please select a subject !!!", Toast.LENGTH_LONG).show();
            }
        });
        Button enterMarks = findViewById(R.id.enterMarks);
        enterMarks.setOnClickListener(v-> {
            if (subject.getSelectedItemPosition() != 0) {

                //Declaring and initializing intent for attendance activity
                Intent intent = new Intent(LoggedIn.this, Marks.class);

                //Adding database referance key name to the intent
                intent.putExtra("Database Referance key", access);

                //Adding the subject chosen by the user
                intent.putExtra("Subject", subject.getSelectedItem().toString());

                //Launching intent
                startActivity(intent);

            } else {
                Toast.makeText(this, "Please select a subject !!!", Toast.LENGTH_LONG).show();
            }
        });

        Button viewAttendance = findViewById(R.id.viewAttendance);
        viewAttendance.setOnClickListener(v -> {
            if (subject.getSelectedItemPosition() != 0) {

                //Declaring and initializing intent for attendance activity
                Intent intent = new Intent(LoggedIn.this, AttendanceViewActivity.class);

                //Adding database referance key name to the intent
                intent.putExtra("Database Referance key", access);

                //Adding the subject chosen by the user
                intent.putExtra("Subject", subject.getSelectedItem().toString());

                intent.putExtra("Show","Attendance");

                //Launching intent
                startActivity(intent);

            } else {
                Toast.makeText(this, "Please select a subject !!!", Toast.LENGTH_LONG).show();
            }
        });

        Button viewMarks = findViewById(R.id.viewMarks);
        viewMarks.setOnClickListener(v -> {
            if (subject.getSelectedItemPosition() != 0) {

                //Declaring and initializing intent for attendance activity
                Intent intent = new Intent(LoggedIn.this, AttendanceViewActivity.class);

                //Adding database referance key name to the intent
                intent.putExtra("Database Referance key", access);

                //Adding the subject chosen by the user
                intent.putExtra("Subject", subject.getSelectedItem().toString());

                intent.putExtra("Show","Marks");

                //Launching intent
                startActivity(intent);

            } else {
                Toast.makeText(this, "Please select a subject !!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //On option selected listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == nav_View.getMenu().getItem(0).getItemId()) {

        }
        return super.onOptionsItemSelected(item);
    }

    //Subject list setting function
    private void subChoser() {
        ArrayAdapter<CharSequence> Subjects;
        switch (section.getSelectedItemPosition()) {
            case 0:
                Toast.makeText(LoggedIn.this, "Please select a Section", Toast.LENGTH_LONG).show();
                break;
            case 1:
                switch (year.getSelectedItemPosition()) {
                    case 0:
                        Toast.makeText(LoggedIn.this, "Select a year", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.csforth, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2015cp";
                        break;
                    case 2:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.csthird, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2016cp";
                        break;
                    case 3:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.cssecond, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2017cp";
                        break;
                    case 4:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.first, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2018cp";
                        break;
                }
                break;
            case 2:
                switch (year.getSelectedItemPosition()) {
                    case 0:
                        Toast.makeText(LoggedIn.this, "Select a year", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.ecforth, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2015ec";
                        break;
                    case 2:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.ecthird, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2016ec";
                        break;
                    case 3:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.ecsecond, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2017ec";
                        break;
                    case 4:
                        Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.first, android.R.layout.simple_spinner_item);
                        Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subject.setAdapter(Subjects);
                        access = "2018ec";
                        break;
                }
                break;
        }
    }
}
