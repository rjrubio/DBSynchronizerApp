package com.example.dbprototypeapp;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.List;

public class AddingCollegeActivity extends AppCompatActivity {

    private View btnAddCollege;
    private EditText input_college;
    private View btnCancel;
    private List<College> collegeList;
    private int status = 0;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_college);

        btnAddCollege = findViewById(R.id.button_add);
        btnCancel = findViewById(R.id.button_cancel);
        checkBox = findViewById(R.id.checkBox_status);
        input_college = findViewById(R.id.input_college_name);

        btnAddCollege.setOnClickListener(onAddCollegeListener());
        btnCancel.setOnClickListener(onCancelListener());
        checkBox.setOnClickListener(onCheckListner());
    }

    private View.OnClickListener onAddCollegeListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (input_college.getText().toString().trim().equals("")) {
                        Toast.makeText(getBaseContext(), "Please input college name", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                        College college = new College();
                        college.setCollegeName(input_college.getText().toString().trim());
                        college.setStatus(checkBox.isChecked()?1:0);
                        college.setCreatedAt(db.getCurrentTimeStamp());
                        try {
                            //save new object to db
                            db.createOrUpdate(college);
                        }catch(SQLException ex){
                            Log.e("College unable to save :", ex.toString());
                        }

                    }
                }

        };
    }

    private View.OnClickListener onCancelListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    private View.OnClickListener onCheckListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    status = 1;
                }
                else{
                    status = 0;
                }
            }
        };
    }

}
