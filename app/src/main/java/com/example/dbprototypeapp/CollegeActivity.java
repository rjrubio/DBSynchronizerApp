package com.example.dbprototypeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.sql.SQLException;
import java.util.List;

public class CollegeActivity extends AppCompatActivity {
    private View btnAddCollege;
    private RecyclerView recycleView;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView.LayoutManager layoutManager;
    List<College> collegeList = null;
    CollegeAdapter dw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);
        recycleView = findViewById(R.id.college_listView);
        swipeLayout = findViewById(R.id.refreshView);
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            collegeList = db.getAll(College.class);
        }catch (SQLException ex){
            Log.e("College Activity :",ex.toString());
        }
        dw = new CollegeAdapter(collegeList,this);
        recycleView.setAdapter(dw);

        ((Main2Activity)getParent()).setFragmentRefreshListener(new Main2Activity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseHelper db = new DatabaseHelper(getParent());
                try {
                    collegeList = db.getAll(College.class);
                    dw = new CollegeAdapter(collegeList,getParent());
                    recycleView.setAdapter(dw);
                    dw.notifyDataSetChanged();
                    Log.e("College ron :","rubio");
                }catch (SQLException ex){
                    Log.e("College Activity :",ex.toString());
                }
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data to your RecyclerView
                onResume();
            }
        });
    }



    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        DatabaseHelper db = new DatabaseHelper(this);
        try {
            collegeList = db.getAll(College.class);
            dw = new CollegeAdapter(collegeList,this);
            recycleView.setAdapter(dw);
            dw.notifyDataSetChanged();
            if (swipeLayout.isRefreshing()) {
                swipeLayout.setRefreshing(false);
            }
        }catch (SQLException ex){
            Log.e("College Activity :",ex.toString());
        }

    }
}
