package com.example.iiitkota;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String Subject;
    private String Access;
    private final ArrayList<List> dataSet;

    public MyAdapter(ArrayList<List> mdataSet, String subject, String access) {
        dataSet = mdataSet;
        Subject = subject;
        Access = access;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final TextView mName;
        final TextView mId;
        final Switch present;

        MyViewHolder(View parent) {
            super(parent);
            mName = parent.findViewById(R.id.name);
            mId = parent.findViewById(R.id.id);
            present = parent.findViewById(R.id.present);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        List data = dataSet.get(i);
        myViewHolder.mId.setText(data.getStudent_ID());
        myViewHolder.mName.setText(data.getStudent_Name());
        myViewHolder.present.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String key = data.getKey();

            HashMap<String, String> sub = data.getAttendance().get(Subject);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Access).child(key).child("attendance").child(Subject);
            if (isChecked) {
                sub.put(new Date().toString(), "Present");
            } else {


                sub.put(new Date().toString(), "NotPresent");
            }
            ref.setValue(sub);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
