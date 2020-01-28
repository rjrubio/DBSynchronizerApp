package com.example.dbprototypeapp;

import android.content.Context;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DoSync {
    void initialize(Context context, RequestQueue requestQueue);
    void doSync();
    String getLocalDBTimeStamp();
    void setLocalDBTimeStamp(String timeStamp);
    void setCloudDBTimeStamp(String timeStamp);
    void setBothTimeStamp(String timeStamp);
    boolean compareTimeStamp(String timestamp1, String Timestamp2);
    void pullLocalDBToCloud(String timestamp);
    void pullCloudDBToLocal(String timestamp);
    void checkConnection(String endpoint);
    void getCloudDBData(String endPoint, final VolleyCallback callback);
    void setCloudDBData(String endPoint, JSONObject jsonObject, final VolleyCallback callback);

}
