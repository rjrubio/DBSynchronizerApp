package com.example.dbprototypeapp;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AppSetting extends AppCompatActivity {
    EditText api = null;
    EditText clientID = null;
    Button save = null;
    SharedPreferencesManager sharedprep= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedprep = new SharedPreferencesManager(this);
        api = findViewById(R.id.api_text);
        clientID = findViewById(R.id.client_id_text);
        api.setText(sharedprep.getKey("API"));
        clientID.setText(sharedprep.getKey("CLIENTID"));
        sharedprep.setKey("API",api.getText().toString());
        sharedprep.setKey("CLIENTID",clientID.getText().toString());
        save = findViewById(R.id.button_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedprep.setKey("API",api.getText().toString());
                sharedprep.setKey("CLIENTID",clientID.getText().toString());
                onBackPressed();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
