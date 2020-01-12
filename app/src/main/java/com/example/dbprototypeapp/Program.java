package com.example.dbprototypeapp;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class Program implements Serializable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private College collegeId;
    @DatabaseField(columnName = "status")
    private int status;
    @DatabaseField(columnName = "years_of_completion")
    private int years_of_completion;
    @DatabaseField(columnName = "program_name")
    private String program_name;
    @DatabaseField(columnName = "created_at")
    private String created_at;

    // constructors
    public Program() {
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCollege(College collegeId) {
        this.collegeId = collegeId;
    }

    public void setYearsOfCompletion(int years_of_completion) {
        this.years_of_completion = years_of_completion;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setProgramName(String program_name){
        this.program_name = program_name;
    }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public College getCollege() {
        return this.collegeId;
    }

    public int getYearsOfCompletion() {
        return this.years_of_completion;
    }

    public int getStatus() {
        return this.status;
    }

    public String getProgramName(){
        return this.program_name;
    }

    public String getCreated_at(){
        return this.created_at;
    }
}