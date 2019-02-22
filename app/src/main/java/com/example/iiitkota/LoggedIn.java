package com.example.iiitkota;

import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoggedIn extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //    DatabaseReference myRef = database.getReference();
    DrawerLayout drawerLayout;
    Spinner year, section, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        //Setting up drawerlayout
        drawerLayout = findViewById(R.id.drawer);
        NavigationView nav_View = findViewById(R.id.nav_view);
        nav_View.setNavigationItemSelectedListener(menuItem -> {
            menuItem.setChecked(true);
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
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //Creating arrayadapters for the spinners
        ArrayAdapter<CharSequence> Years, Sections;

        //creating array adapters
        Years = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        Sections = ArrayAdapter.createFromResource(this, R.array.section, android.R.layout.simple_spinner_item);

        //Specifing Layouts for spinners
        Years.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sections.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Applying adapters to spinners
        year.setAdapter(Years);
        section.setAdapter(Sections);

        //Applying item selection listeners
        section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayAdapter<CharSequence> Subjects;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
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
                                break;
                            case 2:
                                Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.csthird, android.R.layout.simple_spinner_item);
                                Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subject.setAdapter(Subjects);
                                break;
                            case 3:
                                Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.cssecond, android.R.layout.simple_spinner_item);
                                Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subject.setAdapter(Subjects);
                                break;
                            case 4:
                                Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.first, android.R.layout.simple_spinner_item);
                                Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subject.setAdapter(Subjects);
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
                                break;
                            case 2:
                                Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.csthird, android.R.layout.simple_spinner_item);
                                Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subject.setAdapter(Subjects);
                                break;
                            case 3:
                                Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.cssecond, android.R.layout.simple_spinner_item);
                                Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subject.setAdapter(Subjects);
                                break;
                            case 4:
                                Subjects = ArrayAdapter.createFromResource(LoggedIn.this, R.array.first, android.R.layout.simple_spinner_item);
                                Subjects.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subject.setAdapter(Subjects);
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(LoggedIn.this, "Select a year", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
