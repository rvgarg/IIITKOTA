package com.example.iiitkota;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AttendanceFragment extends Fragment {
    private ArrayList<List> listStudents = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_attendance, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        SharedPreferences preferences = getActivity().getSharedPreferences("keys", Context.MODE_PRIVATE);
        String refdb = preferences.getString("Database Referance key", "nan");
        String sub = preferences.getString("Subject", "nan");
        MyAdapter adapter = new MyAdapter(listStudents, sub, refdb);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(refdb);
        ref.addChildEventListener(new ChildEventListener() {
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
        Button subt = getActivity().findViewById(R.id.save);
        subt.setOnClickListener(v -> {
            int total = adapter.getTotalAttendance();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Total Attendance is " + total);
            builder.setPositiveButton("Ok", (dialog, which) -> {
                startActivity(new Intent(getContext(), LoggedIn.class));
                getActivity().finish();
            });
            adapter.notifySavePressed();
            builder.show();
        });
        return recyclerView;
    }
}
