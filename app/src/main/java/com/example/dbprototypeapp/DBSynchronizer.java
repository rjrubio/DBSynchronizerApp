package com.example.dbprototypeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    String clientId = "";
    String api = "";
    SharedPreferencesManager sharedPrep = null;
    ProgressDialog progress = null;

    public void initialize( Context context, RequestQueue requestQueue) {

        this.context = context;
        this.requestQueue = requestQueue;
        db_helper =  new DatabaseHelper(context);
        sharedPrep = new SharedPreferencesManager(context);
        api = sharedPrep.getKey("API");
        clientId = sharedPrep.getKey("CLIENTID");
        progress = new ProgressDialog(context);
        progress.setTitle("Loading");
        progress.setMessage("Synching...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
    }
    @Override
    public void getCloudDBData(String endPoint , final VolleyCallback callback) {
        Log.d("endpoint",endPoint);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, endPoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                        Log.d("getCloudDBData", error.toString());
                    }
                }
        );
        // add it to the RequestQueue
        requestQueue.add(getRequest);
    }

    @Override
    public void setCloudDBData(String endPoint, JSONObject jsonObject, final VolleyCallback callback){

        JsonObjectRequest getRequest = new JsonObjectRequest (Request.Method.POST, endPoint, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                        Log.d("setCloudDBData", error.toString());
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
            public void onResponse(JSONObject result) {
                try {
                        Log.d("API connection :",result.getString("success"));

                } catch (JSONException ex) {
                         Log.e("checkConnection", ex.toString());
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkConnection", error.toString());
            }
        });
    }

    @Override
    public void pullCloudDBToLocal(String timestamp) {
        //TODO
        //RUN ALL TABLE SYNC
        Log.d("rondec time",timestamp);
        DatabaseHelper db =  new DatabaseHelper(context);
        getCloudDBData(api+"college/timecut/"+timestamp, new VolleyCallback() {
            @Override
            public void onResponse(JSONObject result) {
                Log.d("pullCloudDBToLocal",result.toString());
                try {
                    JSONArray resultsdata = result.getJSONArray("data");
                    for (int i = 0; i < resultsdata.length(); i++) {
                        JSONObject jsonobject = resultsdata.getJSONObject(i);
                        jsonobject.remove("id");
                        Gson gson = new Gson();
                        College college = gson.fromJson(jsonobject.toString(), College.class);
                        try {
                            db.createOrUpdate(college);
                            ((Main2Activity)context).getFragmentRefreshListener().onRefresh();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        setBothTimeStamp(db_helper.getCurrentTimeStamp());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("pullCloudDBToLocal", error.toString());
            }});
    }

    @Override
    public void pullLocalDBToCloud(String timestamp) {
        //TODO
        //RUN ALL TABLE SYNCH
        DatabaseHelper db =  new DatabaseHelper(context);
        List<College> collegeList;
        try {
            collegeList = db.getAllWhere(College.class, timestamp);

            try {
                for (int i = 0; i < collegeList.size(); i++) {
                    College collegejsonobject = collegeList.get(i);
                    String jsonInString = new Gson().toJson(collegejsonobject);
                    JSONObject dataToSend = new JSONObject(jsonInString);
                    dataToSend.remove("id");
                    Log.d("data to send", dataToSend.toString());
                    setCloudDBData(api+"college",dataToSend, new VolleyCallback() {
                        @Override
                        public void onResponse(JSONObject result) {
                            Log.d("pullLocalDBToCloud",result.toString());
                            setBothTimeStamp(db_helper.getCurrentTimeStamp());
                        }
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("pullLocalDBToCloud", error.toString());
                        }
                    });
                }
            }catch (JSONException ex){
                Log.e("pullLocalDBToCloud",ex.toString());
            }
            //
        }catch (SQLException ex){
            Log.e("pullLocalDBToCloud:",ex.toString());
        }
        //lastly set cloud local timestamp
    }

    @Override
    public boolean compareTimeStamp(String timestamp1, String timestamp2) {
        return Integer.parseInt(timestamp1) > Integer.parseInt(timestamp2);
    }

    @Override
    public void doSync() {

        progress.show();
        //get Cloud TimeStamp
        getCloudDBData(api+"meta/"+clientId, new VolleyCallback() {
            @Override
            public void onResponse(JSONObject  result) {
                try {
                    //GetLocal TIMESTAMP
                    String cloudTimeStamp = "";
                    String localTimeStamp = getLocalDBTimeStamp();
                    Log.d("local rondec :",localTimeStamp);
                    if(!result.getString("data").isEmpty()  && !localTimeStamp.equals("0")){
                        Log.d("Cloud TimeStamp", result.toString());
                        cloudTimeStamp = result.getString("data");
                        pullLocalDBToCloud(localTimeStamp);
                        pullCloudDBToLocal(cloudTimeStamp);
                    }
                    else{
                        pullLocalDBToCloud("0");
                        pullCloudDBToLocal("0");
                    }

                } catch (JSONException e) {
                    setBothTimeStamp(db_helper.getCurrentTimeStamp());
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
    public void setBothTimeStamp(String timeStamp){
        setLocalDBTimeStamp(timeStamp);
        setCloudDBTimeStamp(timeStamp);
    }

    @Override
    public String getLocalDBTimeStamp() {
        String sqlString = "SELECT MAX(CAST(timestamp AS int)) AS timestamp FROM dbmetadata";
        GenericRawResults<String[]> dbResults= db_helper.queryStatement(DBMetaData.class,sqlString);
        try{
            String str = dbResults.getFirstResult()[0];
            if(str == null){
                str = "0";
            }
            Log.d("timestamp",str);
            return str;
        }catch(SQLException ex){
            Log.e("getLocalDBTimeStamp","no results found");
            return "";
        }
    }

    @Override
    public void setCloudDBTimeStamp(String timeStamp) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("clientid",clientId);
            jsonObject.put("timestamp",timeStamp);
            setCloudDBData(api+"meta",jsonObject, new VolleyCallback() {
                @Override
                public void onResponse(JSONObject result) {
                    Log.d("setCloudDBTimeStamp","success");
                    progress.dismiss();
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("setCloudDBTimeStamp", error.toString());
                    progress.dismiss();
                }
            });
        }catch(JSONException ex){
            Log.e("setCloudDBTimeStamp",ex.toString());
        }

    }

    @Override
    public void setLocalDBTimeStamp(String timestamp) {
        DBMetaData dbmd = new DBMetaData();
        dbmd.setLast_sync_timestamp(timestamp);
        try {
            Log.d("setLocalDBTimeStamp",dbmd.getLast_sync_timestamp());
            db_helper.createOrUpdate(dbmd);
        }catch (SQLException ex){
            Log.e("setLocalDBTimeStamp",ex.toString());
        }
    }
}
