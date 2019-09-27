package com.muilat.restaurant.utilities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.content.IntentCompat;

import com.muilat.restaurant.LoginActivity;

public class SessionManager {

    // Sharedpref file name
    private static final String PREF_NAME = "RestaurantSession";

    // All Shared Preferences Keys
    public static final String IS_LOGGED_IN = "IsLoggedIn";
    public static final String KEY_USERID = "userid";
    // Shared pref mode
    static int PRIVATE_MODE = 0;

    //Authentication
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_TOKEN_TYPE = "token_type";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_AUTH_SCOPE = "scope";

    private static final String KEY_EXPIRATION_TIME = "expiration_time";

    public static void setData(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setData(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setData(Context context, String key, Long value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void setData(Context context, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    /**
     * Clear session details
     * */
    public static void logoutUser(Context context){
        // Clearing all data from Shared Preferences
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();


        Intent intent = new Intent(context, LoginActivity.class);
//        ComponentName cn = intent.getComponent();
//        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();


    }
    // Get Login State
    public static boolean isLoggedIn(Context context){
        return getSessionField(context, IS_LOGGED_IN, false);
    }

    public Long getUserId(Context context){
        return getSessionField(context, KEY_USERID, (long)0);

    }


    public static String getSessionField(Context context, String name_key, String default_value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sp.getString(name_key, default_value);
    }
    public static  int getSessionField(Context context, String name_key, int default_value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sp.getInt(name_key, default_value);
    }
    public static Long getSessionField(Context context, String name_key, Long default_value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sp.getLong(name_key, default_value);
    }
//    public float getSessionField(Context context, String name_key, float default_value){
//        return pref.getFloat(name_key, 0);
//    }

    public static boolean getSessionField(Context context, String name_key, boolean default_value){
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sp.getBoolean(name_key, default_value);
    }

}
