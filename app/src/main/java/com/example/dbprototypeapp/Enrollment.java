package com.example.dbprototypeapp;


import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class Enrollment implements Serializable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Person student_id;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private Subject subject_id;
    @DatabaseField(columnName = "grade")
    private int grade;
    @DatabaseField(columnName = "status")
    private int status;
    @DatabaseField(columnName = "created_at")
    private String created_at;

    // constructors
    public Enrollment() {
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStudentId(Person student_id) {
        this.student_id = student_id;
    }

    public void setSubjectId(Subject subject_id) {
        this.subject_id = subject_id;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public Person getStudentId() {
        return this.student_id;
    }

    public Subject getSubjectId() {
        return this.subject_id;
    }

    public int getGrade() {
        return this.grade;
    }

    public int getStatus() {
        return this.status;
    }

    public String getCreated_at(){
        return this.created_at;
    }
}