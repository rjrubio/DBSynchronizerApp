package com.example.dbprototypeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DrawerRecyclerViewAdapter extends OrmliteCursorRecyclerViewAdapter<College, DrawerRecyclerViewAdapter.ViewHolder> {
    public DrawerRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public DrawerRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text_holder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, College college) {
        holder.drawerTextView.setText(college.getCollegeName());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView drawerTextView;

        public ViewHolder(View drawerView) {
            super(drawerView);
            drawerTextView = (TextView) drawerView.findViewById(R.id.college_listView);
        }
    }
}