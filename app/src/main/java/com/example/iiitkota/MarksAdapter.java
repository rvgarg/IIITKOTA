package com.example.iiitkota;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.MyViewHolderMarks> {
    private final String Subject;
    private final String Access;
    private final ArrayList<List> dataSet;
    private HashMap<String, Integer> mark = new HashMap<>();
    public boolean savePressed = false;
    HashMap<String, String> dat = new HashMap<>();

    //Declaring Constructor
    public MarksAdapter(ArrayList<List> mdataSet, String subject, String access) {
        dataSet = mdataSet;
        Subject = subject;
        Access = access;
    }

    //Declaring inner class for recycler view view holder customisation
    public static class MyViewHolderMarks extends RecyclerView.ViewHolder {
        //Declaring the components of an individual data item
        final TextView mName;
        final TextView mId;
        final EditText present;

        MyViewHolderMarks(View parent) {
            super(parent);

            //Initializing the parts of the recycling layout
            mName = parent.findViewById(R.id.mname);
            mId = parent.findViewById(R.id.mid);
            present = parent.findViewById(R.id.m_enter);
        }


    }

    @NonNull
    @Override
    public MarksAdapter.MyViewHolderMarks onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        //Creating view to be inserted to the recycler view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marks_layout, parent, false);
        return new MarksAdapter.MyViewHolderMarks(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksAdapter.MyViewHolderMarks myViewHolder, int i) {

        //Getting data item at this position
        List data = dataSet.get(i);

        //Setting the corresponding values to the components
        myViewHolder.mId.setText(data.getStudent_ID());
        myViewHolder.mName.setText(data.getStudent_Name());
        myViewHolder.present.setText("");

        //Setting up state change listener
        myViewHolder.present.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        dat.put(data.getKey(), s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );
//        myViewHolder.present.setOnFocusChangeListener((v, hasFocus) -> {
//            if (!hasFocus) {
//                int marks;
//                if (TextUtils.isEmpty(myViewHolder.present.getText().toString())) {
//                    marks = 0;
//                } else {
//                    marks = Integer.parseInt(myViewHolder.present.getText().toString().trim());
//                }
//                //Updating data to the database
//                Log.e("checking", "reached");
//                String key = data.getKey();
//                HashMap<String, String> sub = data.getMarks().get(Subject);
//                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(Access).child(key).child("marks").child(Subject);
//                sub.put(new Date().toString(), "" + marks);
//                sub.remove("init");
//                ref.setValue(sub);
//                mark.put(data.getStudent_Name(), marks);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    HashMap<String, Integer> getMark() {
        return mark;
    }

    void notifySavePressed() {
        for (HashMap.Entry<String, String> it : dat.entrySet()
        ) {
            Log.e("data", "sent");
            FirebaseDatabase.getInstance().getReference().child(Access).child(it.getKey()).child("marks").child(Subject).child(new Date().toString()).setValue(it.getValue());
        }

        Log.e("chk", "save pressed");
        savePressed = true;
    }
}
