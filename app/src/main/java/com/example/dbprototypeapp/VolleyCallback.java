package com.example.dbprototypeapp;

import com.android.volley.VolleyError;

import org.json.JSONArray;

public interface VolleyCallback {
    void onResponse(JSONArray result);
    void onErrorResponse(VolleyError error);
}
