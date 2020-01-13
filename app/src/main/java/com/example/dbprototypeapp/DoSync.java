package com.example.dbprototypeapp;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

public interface DoSync {
    void initialize(Context context, RequestQueue requestQueue);
    void doSync();
    String getLocalDBTimeStamp();
    void setLocalDBTimeStamp();
    void setCloudDBTimeStamp();
    boolean compareTimeStamp(String timestamp1, String Timestamp2);
    void pullLocalDBToCloud();
    void pullCloudDBToLocal();
    void checkConnection(String endpoint);
    void getCloudDBData(String endPoint, final VolleyCallback callback);
    void setCloudDBData(String endPoint, JSONArray jsonObject, final VolleyCallback callback);

}
