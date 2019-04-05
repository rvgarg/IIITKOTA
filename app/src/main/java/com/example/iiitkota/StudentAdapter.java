package com.example.iiitkota;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
class StudentAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> dataHeader;
    private HashMap<String,ArrayList<Pair<String,String>>> dataList;

    public StudentAdapter(Context context, List<String> dataHeader, HashMap<String, ArrayList<Pair<String, String>>> dataList){
        this.mContext = context;
        this.dataHeader = dataHeader;
        this.dataList = dataList;
    }
    @Override
    public int getGroupCount() {
        return dataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataList.get(this.dataHeader.get(groupPosition))
                .size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return dataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (dataList.get(this.dataHeader.get(groupPosition)).size() == 1){
            return new Pair<>("No entry has ", "been done yet !!!");
        }
        return dataList.get(this.dataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.student, null);
        }
        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Pair<String,String> childText = (Pair<String, String>) getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater innfalInflator = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = innfalInflator.inflate(R.layout.item,null);
        }
        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);
        TextView txtChildPrecence = convertView.findViewById(R.id.lblPresence);

        txtListChild.setText(childText.first);
        txtChildPrecence.setText(childText.second);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
