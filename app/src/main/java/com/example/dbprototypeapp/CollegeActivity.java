package com.example.dbprototypeapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.SQLException;
import java.util.List;

public class CollegeActivity extends AppCompatActivity {
    private View btnAddCollege;
    private RecyclerView recycleView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        btnAddCollege = findViewById(R.id.button_add_college);
        recycleView = findViewById(R.id.college_listView);
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        DatabaseHelper db = new DatabaseHelper(this);
        List<College> collegeList = null;
        try {
            collegeList = db.getAll(College.class);
        }catch (SQLException ex){
            Log.e("College Activity :",ex.toString());
        }
        CollegeAdapter dw = new CollegeAdapter(collegeList,this);
        recycleView.setAdapter(dw);

        btnAddCollege.setOnClickListener(onAddCollegeListener());
    }

    private View.OnClickListener onAddCollegeListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start NewActivity.class
                Intent myIntent = new Intent(CollegeActivity.this,
                        AddingCollegeActivity.class);
                startActivity(myIntent);

            }
        };
    }

}
