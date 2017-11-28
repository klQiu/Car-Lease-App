package com.example.elvis.carleaseapp;

/**
 * Created by BETTY on 2017/11/13.
 */

public class Current {
    private static User curUser;

    public static void addCurUser(User user){
        curUser = new User(user.getEmail(), user.getPassword(), user.getID());
    }


    public static int getCurUserID()    {
        if(curUser != null)
            return curUser.getID();
        else
            return 0;
    }

    public static void deleteCurUser(){
        curUser = null;
    }

    public static User getCurUser(){
        return curUser;
    }

//    /**
//     * Saves a user to the shared prefs of the device. The user is serialized to allow for shared pref save
//     *
//     * @param currentUser
//     */
//    public static void saveUserToPref(User currentUser) {
//        String userString = currentUser.serializeUser();
//        try {
//            PreferenceHelper.getPreferenceHelperInstance().put(StringUtils.UserKey, userString);
//        } catch (Exception e) {
//            Log.e(TAG, "Save user error:" + e.getMessage());
//        }
//    }
//
//    /**
//     * Looks for the shared pref saved user and deserializes it back into User.class
//     *
//     * @return User
//     */
//    @Nullable
//    public User retrieveUserFromPref() {
//        try {
//            PreferenceHelper sharedPref = PreferenceHelper.getPreferenceHelperInstance();
//            String userString = sharedPref.getString(StringUtils.UserKey, "");
//            if (!userString.isEmpty()) {
//                return User.createUser(userString);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Could not retrieve user: " + e.getMessage());
//        }
//
//        return null;
//    }
}

