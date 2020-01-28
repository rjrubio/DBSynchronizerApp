package com.example.dbprototypeapp;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class College implements Serializable {
    private static final long serialVersionUID = -222864131214757024L;
    @DatabaseField(generatedId = true, columnName = "id")
    private int id;
    @DatabaseField(columnName = "status")
    private String status;
    @DatabaseField(columnName = "college_name")
    private String college_name;
    @DatabaseField(columnName = "created_at")
    private String created_at;

    // constructors
    public College() {
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCollegeName(String college_name){
        this.college_name = college_name;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public String getStatus() {
        return this.status;
    }

    public String getCollegeName(){
        return this.college_name;
    }

    public String getCreated_at(){
        return this.created_at;
    }
}