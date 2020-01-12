package com.example.dbprototypeapp;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class Subject implements Serializable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private College collegeId;
    @DatabaseField(columnName = "units")
    private int units;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Person faculty_id;
    @DatabaseField(columnName = "status")
    private int status;
    @DatabaseField(columnName = "subject_name")
    private String subject_name;
    @DatabaseField(columnName = "created_at")
    private String created_at;

    // constructors
    public Subject() {
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCollege(College collegeId) {
        this.collegeId = collegeId;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setFacultyId(Person faculty_id) {
        this.faculty_id = faculty_id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSubjectName(String subject_name){
        this.subject_name = subject_name;
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

    public int getUnits() {
        return this.units;
    }

    public Person getFacultyId() {
        return this.faculty_id;
    }

    public int getStatus() {
        return this.status;
    }

    public String getSubjectName(){
        return this.subject_name;
    }

    public String getCreated_at(){
        return this.created_at;
    }
}