package com.example.dbprototypeapp;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    Context mcontex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue rq = Volley.newRequestQueue(this);
        mcontex = this;

        //this.deleteDatabase("student_manager.db");
        Button collegeBtn = findViewById(R.id.button_college);
        Button syncBtn = findViewById(R.id.button_synch);

        // Capture button clicks
        collegeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        CollegeActivity.class);
                startActivity(myIntent);
            }
        });

        syncBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                DBSynchronizer dbs = new DBSynchronizer();
                dbs.initialize(mcontex, rq);
                dbs.doSync();
            }
        });
    }
}
