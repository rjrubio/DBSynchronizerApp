package com.example.dbprototypeapp;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

public interface DoSync {
    void initialize(Context context, RequestQueue requestQueue);
    void doSync();
    String getLocalDBTimeStamp();
    void setLocalDBTimeStamp();
    String getCloudDBTimeStamp();
    void setCloudDBTimeStamp();
    String compareTimeStamp(String timestamp1, String Timestamp2);
    int syncLocalDB();
    int syncCloudDB();
    int checkConnection();
    void syncCloudDBTableData(String endPoint, String table_name, JSONArray jsonObject);
    void synLocalDBTableData(String endPoint, String table_name);
    void getCloudDBData(String endPoint, String table_name , final VolleyCallback callback);
    void setCloudDBData(String endPoint, String table_name , JSONArray jsonObject, final VolleyCallback callback);

}
