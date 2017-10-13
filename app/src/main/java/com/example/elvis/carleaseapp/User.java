package com.example.elvis.carleaseapp;

/**
 * Created by apple on 2017/10/12.
 */

public class User {
    private String email;
    private String password;
    private int userID;

    public User(String email, String password, int userID) {
        this.email = email;
        this.password = password;
        this.userID = userID;
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
}


