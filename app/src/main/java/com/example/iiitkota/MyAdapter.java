package com.example.iiitkota;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private ArrayList<List> dataSet;
    public MyAdapter(ArrayList<List> mdataSet){
        dataSet = mdataSet;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mName,mId;
        Switch present;
        MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.recycler_layout,parent,false));
            mName = parent.findViewById(R.id.name);
            mId = parent.findViewById(R.id.id);
            present = parent.findViewById(R.id.present);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mId.setText(dataSet.get(i).getmId());
        myViewHolder.mName.setText(dataSet.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
