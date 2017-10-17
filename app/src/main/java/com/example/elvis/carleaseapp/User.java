package com.example.elvis.carleaseapp;

/**
 * Created by apple on 2017/10/12.
 */

public class User {
    private String email;
    private String password;
    private int userID;

    public User(){
        this.email = "";
    };
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

    public void setID(int id) {
        this.userID = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}


