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

    public void initialize( Context context, RequestQueue requestQueue) {

        this.context = context;
        this.requestQueue = requestQueue;
        db_helper =  new DatabaseHelper(context);
    }
    @Override
    public void getCloudDBData(String endPoint , final VolleyCallback callback) {

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
    public void setCloudDBData(String endPoint, JSONArray jsonObject, final VolleyCallback callback){

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
    public void checkConnection(String endpoint) {
        getCloudDBData(endpoint, new VolleyCallback() {
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
    }

    @Override
    public void pullCloudDBToLocal() {
        //TODO
        //RUN ALL TABLE SYNC
        DatabaseHelper db =  new DatabaseHelper(context);

        //college
        List<College> collegeList = null;
        JSONArray data = new JSONArray(collegeList);
        getCloudDBData("https://jsonplaceholder.typicode.com/todos", new VolleyCallback() {
            @Override
            public void onResponse(JSONArray result) {
                Log.d("College Response TimeStamp","success");
                try {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonobject = result.getJSONObject(i);
                        Gson gson = new Gson();
                        College college = gson.fromJson(jsonobject.toString(), College.class);
                        try {
                            db.createOrUpdate(college);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("College Error.Response", error.toString());
            }});
        //lastly set cloud local timestamp
        setCloudDBTimeStamp();
    }

    @Override
    public void pullLocalDBToCloud() {
        //TODO
        //RUN ALL TABLE SYNCH
        DatabaseHelper db =  new DatabaseHelper(context);
        List<College> collegeList = null;
        try {
            collegeList = db.getAll(College.class);
            JSONArray data = new JSONArray(collegeList);
            setCloudDBData("https://jsonplaceholder.typicode.com/todos",data, new VolleyCallback() {
                @Override
                public void onResponse(JSONArray result) {
                        Log.d("College Response TimeStamp","success");
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("College Error.Response", error.toString());
                }
            });
            //
        }catch (SQLException ex){
            Log.e("College pullLocalDBToCloud :",ex.toString());
        }
        //lastly set cloud local timestamp
        setLocalDBTimeStamp();
    }

    @Override
    public boolean compareTimeStamp(String timestamp1, String timestamp2) {
        return Integer.parseInt(timestamp1) > Integer.parseInt(timestamp2);
    }

    @Override
    public void doSync() {
        //get Cloud TimeStamp
        getCloudDBData("https://jsonplaceholder.typicode.com/todos", new VolleyCallback() {
            @Override
            public void onResponse(JSONArray result) {
                try {
                    JSONObject jsonobject = result.getJSONObject(0);
                    Log.d("Cloud Response TimeStamp", jsonobject.toString());
                    //TODO
                    //GetLocal TIMESTAMP
                    String cloudTimeStamp = "";
                    String localTimeStamp = getLocalDBTimeStamp();
                    //Compare
                    if(compareTimeStamp(localTimeStamp, cloudTimeStamp)){
                        //Local is outdated
                        //call pull cloud DB -> local DB
                        pullCloudDBToLocal();
                    }
                    else{
                        //Cloud is outdated
                        //call pull local DB -> Cloud DB
                        pullLocalDBToCloud();
                    }


                } catch (JSONException e) {
                    Log.e("DOSYNCH:", e.toString());
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DOSYNCH Error.Response", error.toString());
            }
        });


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
}
