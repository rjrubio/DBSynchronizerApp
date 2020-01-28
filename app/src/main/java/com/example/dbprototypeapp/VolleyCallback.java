package com.example.dbprototypeapp;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyCallback {
    void onResponse(JSONObject result);
    void onErrorResponse(VolleyError error);
}
