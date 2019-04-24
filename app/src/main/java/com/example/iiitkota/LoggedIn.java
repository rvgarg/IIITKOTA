package com.example.iiitkota;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoggedIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Spinner year;
    private Spinner subject;
    private Spinner section;
    private String access;
    private NavigationView nav_View;
    private LinearLayout Main, Fabs;
    private boolean cncled = false;

    @Override
    protected void onResume() {
        if (!cncled)
            netChk();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        //Initializing the drawer layout
        drawerLayout = findViewById(R.id.drawer);
        if (!cncled)
            netChk();

        //Getting firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //Initializing the navigation view
        nav_View = findViewById(R.id.nav_view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View header = nav_View.getHeaderView(0);

        TextView nam = header.findViewById(R.id.nam);
        TextView tid = header.findViewById(R.id.id);
        ImageView proPic = header.findViewById(R.id.imageView);

        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null)
            Glide.with(this).load(user.getPhotoUrl()).placeholder(R.drawable.ic_person_recycle_24dp).into(proPic);

        nam.setText(user.getDisplayName() + " ");
        tid.setText(user.getEmail());

        //Setting up the listener on navigation view
        nav_View.setNavigationItemSelectedListener(menuItem -> {
//            menuItem.setChecked(true);
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.set:
                    startActivity(new Intent(LoggedIn.this, SettingActivity.class));
                    break;
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

        Main = findViewById(R.id.main);
        Fabs = findViewById(R.id.fabs);

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

        Button Next = findViewById(R.id.next);
        Next.setOnClickListener(v -> {
            if (year.getSelectedItemPosition() != 0) {
                if (subject.getSelectedItemPosition() != 0) {
                    Next.setVisibility(View.GONE);
                    Fabs.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "Please select a subject !!!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please select a year !!!", Toast.LENGTH_LONG).show();
            }
        });


        Button enter = findViewById(R.id.enter);
        enter.setOnClickListener(v -> {
            startActivity(new Intent(LoggedIn.this,Attendance.class));
        });

        Button view = findViewById(R.id.viewd);
        view.setOnClickListener(v -> startActivity(new Intent(LoggedIn.this,AttendanceViewActivity.class)));

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

    @Override
    public void onBackPressed() {
        if (Main.getVisibility() == View.GONE) {
            Main.setVisibility(View.VISIBLE);
            Fabs.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    public void netChk() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Boolean isWifiConn = false;
        Boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        if (isMobileConn || isWifiConn)
            new NetworkCheck(LoggedIn.this).execute(isWifiConn, isMobileConn);
        else {
            BottomSheetDialog dialog = new BottomSheetDialog(LoggedIn.this);
            dialog.setContentView(R.layout.connection_manager);
            dialog.setTitle("Not Connected ???");
            dialog.findViewById(R.id.btncancel).setOnClickListener(v -> {
                dialog.dismiss();
            });
            dialog.findViewById(R.id.btnsubt).setOnClickListener(v -> {
                boolean mobile, wifi;
                mobile = ((Switch) dialog.findViewById(R.id.mobileDataState)).isChecked();
                wifi = ((Switch) dialog.findViewById(R.id.wifiState)).isChecked();
                if (wifi) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                }
                if (mobile) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    startActivity(intent);
                }
            });
            dialog.show();
        }
    }
}
