package com.example.signal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefManager  {

    public String Key_Access_Token = "token";
    public static final String Shared_pref_Name = "FCM Data";
    SharedPreferences sharedPreferences;

    private static Context context;
    private static SharedPrefManager mInstance;

    public SharedPrefManager( Context cntxt){
        this.sharedPreferences = cntxt.getSharedPreferences(Shared_pref_Name,Context.MODE_PRIVATE);
    }



    public boolean storeToken(String token){
        Log.d(Shared_pref_Name,"Token storage start");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Key_Access_Token,token).apply();
        return true;

    }

    public String getToken(){

        String tok = sharedPreferences.getString(Key_Access_Token,null);
        Log.d(Shared_pref_Name,"Got the token"+tok);
        return tok;
    }
}
