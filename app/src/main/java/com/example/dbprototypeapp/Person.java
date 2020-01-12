package com.example.dbprototypeapp;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class Person implements Serializable {
    @DatabaseField(generatedId = true, columnName = "id")
    private int id;
    @DatabaseField(columnName = "age")
    private int age;
    @DatabaseField(columnName = "gender")
    private int gender;
    @DatabaseField(columnName = "status")
    private int status;
    @DatabaseField(columnName = "first_name")
    private String first_name;
    @DatabaseField(columnName = "last_name")
    private String last_name;
    @DatabaseField(columnName = "middle_name")
    private String middle_name;
    @DatabaseField(columnName = "address")
    private String address;
    @DatabaseField(columnName = "created_at")
    private String created_at;
    @DatabaseField(columnName = "hash")
    private String hash;

    // constructors
    public Person() {
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setFirstName(String first_name){
        this.first_name = first_name;
    }

    public void setLastName(String last_name){
        this.last_name = last_name;
    }

    public void setMiddleName(String middle_name){
        this.middle_name = middle_name;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setCreatedAt(String created_at){
        this.created_at = created_at;
    }

    public void setHash(String hash){
        this.hash = hash;
    }

    // getters
    public long getId() {
        return this.id;
    }

    public int getAge() {
        return this.age;
    }

    public int getGender() {
        return this.gender;
    }

    public int getStatus() {
        return this.status;
    }

    public String getFirstName(){
        return this.first_name;
    }

    public String getLastName(){
        return this.last_name;
    }

    public String geMiddleName(){
        return this.middle_name;
    }

    public String getAddress(){
        return this.address;
    }

    public String getCreated_at(){
        return this.created_at;
    }

    public String getHash(){
        return this.hash;
    }
}