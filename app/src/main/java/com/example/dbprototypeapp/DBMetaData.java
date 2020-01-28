package com.example.dbprototypeapp;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;


public class DBMetaData implements Serializable {
    @DatabaseField(generatedId = true, columnName = "id")
    private  int id;
    @DatabaseField(columnName = "timestamp")
    private String timestamp;


    public DBMetaData() {
    }

    public String getLast_sync_timestamp() {
        return this.timestamp;
    }

    public void setLast_sync_timestamp(String last_sync_timestamp) {
        this.timestamp = last_sync_timestamp;
    }
}
