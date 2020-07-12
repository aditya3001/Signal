package com.example.signal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Profile;
import com.zerodhatech.models.User;

import org.json.JSONException;

import java.io.IOException;

public class MyThread extends Thread {
    KiteConnect kiteSdk = new KiteConnect("o2u3tpulm3z3agny");
    public String Profile_name;
    private String request_token;
    Intent intent;
    private String public_token;
    private String AccessToken;
    Margin.Available Current_balance;

    public MyThread(Intent intent){
        this.intent = intent;
    }

    @Override
    public void run() {

        try {
            kiteSdk.setUserId("");
            Log.d("THREAD","We are in thread");
            //need to keep it hidden
            String api_secret_key = "";
            request_token = intent.getStringExtra("request_token");
            Log.d("THREAD",request_token);
            User user = kiteSdk.generateSession(request_token, api_secret_key);
            public_token = user.publicToken;
            AccessToken = user.accessToken;
            kiteSdk.setAccessToken(AccessToken);
            kiteSdk.setPublicToken(public_token);
            Profile profile = kiteSdk.getProfile();
            Margin margin = kiteSdk.getMargins("equity");
            this.Profile_name = profile.userName;
            this.Current_balance = margin.available;

//            SharedPreferences sharedPref = getParent().getPreferences(Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("access_token", user.accessToken);
//            editor.commit();

        } catch (KiteException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
