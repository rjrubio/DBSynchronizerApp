package com.example.dbprototypeapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class DBSynchronizer implements DoSync{
    private RequestQueue requestQueue = null;
    private  Context context = null;
    DatabaseHelper db_helper = null;
    JSONArray genericResponse = null;

    public void initialize( Context context, RequestQueue requestQueue) {

        this.context = context;
        this.requestQueue = requestQueue;
        db_helper =  new DatabaseHelper(context);
    }
    @Override
    public void getCloudDBData(String endPoint, String table_name , final VolleyCallback callback) {

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, endPoint, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        // add it to the RequestQueue
        requestQueue.add(getRequest);
    }

    @Override
    public void setCloudDBData(String endPoint, String table_name , JSONArray jsonObject, final VolleyCallback callback){

        JsonArrayRequest getRequest = new JsonArrayRequest (Request.Method.PUT, endPoint, jsonObject,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        // add it to the RequestQueue
        requestQueue.add(getRequest);
    }
    @Override
    public void syncCloudDBTableData(String endPoint, String table_name, JSONArray jsonObject) {

        getCloudDBData(endPoint, table_name, new VolleyCallback() {
            @Override
            public void onResponse(JSONArray result) {
                try {
                    genericResponse = result;
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonobject = result.getJSONObject(i);
                        Log.d("Response", jsonobject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
    }

    @Override
    public int checkConnection() {
        getCloudDBData("", "", new VolleyCallback() {
            @Override
            public void onResponse(JSONArray result) {
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonobject = result.getJSONObject(i);
                        Log.d("Response", jsonobject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
            }
        });
        return 0;
    }

    @Override
    public int syncCloudDB() {
        //TODO
        //RUN ALL TABLE SYNC
        return 0;
    }

    @Override
    public int syncLocalDB() {
        //TODO
        //RUN ALL TABLE SYNCH
        return 0;
    }

    @Override
    public String compareTimeStamp(String timestamp1, String timestamp2) {
        if (Integer.parseInt(timestamp1) > Integer.parseInt(timestamp2))
            return timestamp1;
        else
            return timestamp2;
    }

    @Override
    public String getCloudDBTimeStamp() {
        DBMetaData dbmd = new DBMetaData();
        dbmd.setLast_sync_timestamp(db_helper.getCurrentTimeStamp());
        Gson gson = new Gson();
        String jsonString = gson.toJson(dbmd);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray(jsonObject);
            syncCloudDBTableData("endPoint", "table_name", jsonArray);
            Log.d("Response", genericResponse.getJSONObject(0).toString());
            return genericResponse.getJSONObject(0).toString();
        }catch(JSONException ex){
            Log.e("JSON error","SetCloud Timestamp Converting jason");
        }
        return null;
    }

    @Override
    public String getLocalDBTimeStamp() {
        String sqlString = "SELECT * FROM dbmetadata ORDER BY last_sync_timestamp ASC LIMIT 1";
        GenericRawResults<String[]> dbResults= db_helper.queryStatement(DBMetaData.class,sqlString);
        try{
            String[] str = dbResults.getResults().get(0);
            return String.join(" ", str);
        }catch(SQLException ex){
            Log.e("DB debug ","no results found");
            return "";
        }
    }

    @Override
    public void setCloudDBTimeStamp() {
        DBMetaData dbmd = new DBMetaData();
        dbmd.setLast_sync_timestamp(db_helper.getCurrentTimeStamp());
        Gson gson = new Gson();
        String jsonString = gson.toJson(dbmd);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = new JSONArray(jsonObject);
            syncCloudDBTableData("endPoint", "table_name", jsonArray);
        }catch(JSONException ex){
            Log.e("JSON error","SetCloud Timestamp Converting jason");
        }

    }

    @Override
    public void setLocalDBTimeStamp() {
        DBMetaData dbmd = new DBMetaData();
        dbmd.setLast_sync_timestamp(db_helper.getCurrentTimeStamp());
        try {
            db_helper.createOrUpdate(dbmd);
        }catch (SQLException ex){
            Log.e("DB error",ex.toString());
        }
    }

    @Override
    public void synLocalDBTableData(String endPoint, String table_name) {

    }

    @Override
    public void doSync() {
        //ToDo
        //Get Local and Cloud TimeStamp
        //Compare
        //IF LOCAL
        //Do sync local db
        //get cloud db
        //set local db
        //ELSE
        //Do synch cloud db
        //get local db
        //set cloud db

        //update ui
    }
}
