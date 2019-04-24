package com.example.iiitkota;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class MarksV extends Fragment {
    private StudentAdapter adapter;
    private ArrayList<String> headerList = new ArrayList<>();
    private HashMap<String, ArrayList<Pair<String, String>>> childList = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_attendance_view, container, false);
        ExpandableListView listView = view.findViewById(R.id.expstu);
        SharedPreferences preferences = getActivity().getSharedPreferences("keys", Context.MODE_PRIVATE);
        String refdb = preferences.getString("Database Referance key", "nan");
        String sub = preferences.getString("Subject", "nan");
        Button subt = getActivity().findViewById(R.id.save);
        subt.setVisibility(View.GONE);
        adapter = new StudentAdapter(getContext(), headerList, childList);

        listView.setAnimation(new AlphaAnimation((float) 0.2, (float) 1.0));

        listView.setAdapter(adapter);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(refdb);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Getting the child from database and parsing it to list class object
                List list = dataSnapshot.getValue(List.class);

                // Setting up the key for the data in the list object
                list.setKey(dataSnapshot.getKey());

                // Adding list object to the ArrayList of dataset

                headerList.add(list.getStudent_ID());

                ArrayList<Pair<String, String>> str = new ArrayList<>();
                for (HashMap.Entry<String, String> it : list.getMarks().get(sub).entrySet()) {
                    str.add(new Pair<>(it.getKey(), it.getValue()));
                }
                childList.put(list.getStudent_ID(), str);

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
        return listView;
    }
}
