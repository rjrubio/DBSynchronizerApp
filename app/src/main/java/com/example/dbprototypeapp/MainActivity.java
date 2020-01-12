package com.example.dbprototypeapp;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper db =  new DatabaseHelper(getApplicationContext());
        Person student = new Person();

        student.setFirstName("XYZ");
        student.setLastName("ABC");
        student.setAge(12);
        student.setAddress("USA");
        Gson gson = new Gson();
        String json = gson.toJson(student);
        String hash = db.stringToSha256(json);

        student.setHash(hash);
        Log.d("rondec",json);

        try {
            db.createOrUpdate(student);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        RequestQueue rq = Volley.newRequestQueue(this);
        Context mcontex = this;
        DBSynchronizer dbs = new DBSynchronizer();
        dbs.initialize(mcontex, rq);
        dbs.setLocalDBTimeStamp();
        Log.e("rubio: ",dbs.getLocalDBTimeStamp());


        Button collegeBtn = findViewById(R.id.button_college);

        // Capture button clicks
        collegeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(MainActivity.this,
                        CollegeActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
