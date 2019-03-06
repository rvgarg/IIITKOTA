package com.example.iiitkota;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MyViewHolderMarks> {
    private final String Subject;
    private final String Access;
    private final ArrayList<List> dataSet;

    public MarksAdapter(ArrayList<List> mdataSet, String subject, String access) {
        dataSet = mdataSet;
        Subject = subject;
        Access = access;
    }

    public static class MyViewHolderMarks extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final TextView mName;
        final TextView mId;
        final EditText present;

        MyViewHolderMarks(View parent) {
            super(parent);
            mName = parent.findViewById(R.id.mname);
            mId = parent.findViewById(R.id.mid);
            present = parent.findViewById(R.id.m_enter);
        }


    }

    @NonNull
    @Override
    public MarksAdapter.MyViewHolderMarks onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marks_layout, parent, false);
        return new MarksAdapter.MyViewHolderMarks(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksAdapter.MyViewHolderMarks myViewHolder, int i) {
        List data = dataSet.get(i);
        myViewHolder.mId.setText(data.getStudent_ID());
        myViewHolder.mName.setText( data.getStudent_Name() );
        myViewHolder.present.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int marks = Integer.parseInt(myViewHolder.present.getText().toString().trim());
                String key = data.getKey();
                HashMap<String, String> sub = data.getMarks().get(Subject);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Access).child(key).child("marks").child(Subject);
                sub.put(new Date().toString(), "" + marks);
                ref.setValue(sub);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}