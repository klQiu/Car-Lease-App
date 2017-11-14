package com.example.elvis.carleaseapp;

/**
 * Created by BETTY on 2017/11/13.
 */

public class Current {
    private static User curUser = null;

    public static void addCurUser(User user){
        curUser = new User(user.getEmail(), user.getPassword(), user.getID());
    }

    public static void deleteCurUser(){
        curUser = null;
    }
}
