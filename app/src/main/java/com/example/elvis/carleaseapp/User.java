package com.example.elvis.carleaseapp;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String password;
    private int userID;

    private List<String> starredPostIds = new ArrayList<>();

    private static final String TAG = User.class.getSimpleName();

    public User(){
        this.email = "";
    };
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(String email, String password, int id) {
        this.email = email;
        this.password = password;
        this.userID = id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public int getID() {
        return this.userID;
    }

    public void setID(int id) {
        this.userID = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStarredPostId(List<String> starredPostIds) {
        this.starredPostIds = starredPostIds;
    }

    public List<String> getStarredPostIds() {
        return this.starredPostIds;
    }

    public static User createUser(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public String serializeUser() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}

