package com.example.dbprototypeapp;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;

public interface VolleyCallback {
    void onResponse(JSONArray result);
    void onErrorResponse(VolleyError error);
}
