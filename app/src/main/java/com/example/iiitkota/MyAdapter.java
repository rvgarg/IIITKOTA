package com.example.iiitkota;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private final ArrayList<List> dataSet;
    public MyAdapter(ArrayList<List> mdataSet){
        dataSet = mdataSet;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        final TextView mName;
        final TextView mId;
        final Switch present;
        MyViewHolder( View parent){
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
        myViewHolder.mId.setText(data.getId());
        myViewHolder.mName.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
