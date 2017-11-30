package com.example.elvis.carleaseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by BETTY on 2017/11/13.
 */

public class Current {
    private static User curUser;
    private static final String USER_KEY = "user_key";
    private static final String TAG = Current.class.getSimpleName();

    public static void addCurUser(User user, Context context){
        Log.v(TAG, "adding cur user with shared pref");
        curUser = new User(user.getEmail(), user.getPassword(), user.getID());
        curUser.setStarredPostId(user.getStarredPostIds());
        saveUserToPref(curUser, context);
    }

    /**
     * Adds a saved user to Current user instance
     * @param user
     */
    public static void addCurUser(User user){
        curUser = user;
    }


    public static int getCurUserID()    {
        if(curUser != null)
            return curUser.getID();
        else
            return 0;
    }

    public static void deleteCurUser(Context context){
        curUser = null;
        saveUserToPref(null, context);
    }

    public static User getCurUser(){
        return curUser;
    }

    /**
     * Saves a user to the shared prefs of the device. The user is serialized to allow for shared pref save
     *
     * @param currentUser
     */
    public static void saveUserToPref(User currentUser, Context context) {
        Log.v(TAG, "saving user to pref");
        String userString;
        if(currentUser == null) {
            userString = "";
        }
        else {
            userString = currentUser.serializeUser();
        }
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(USER_KEY, userString);
        editor.apply();
    }

    /**
     * Looks for the shared pref saved user and deserializes it back into User.class
     *
     * @return User
     */
    @Nullable
    public static User retrieveUserFromPref(Context context) {
        Log.v(TAG, "retrieving user from pref");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String userString = sharedPrefs.getString(USER_KEY, "");
        if (!userString.isEmpty()) {
            return User.createUser(userString);
        }
        else {
            return null;
        }
    }
}

