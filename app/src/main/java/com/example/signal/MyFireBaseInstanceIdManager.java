package com.example.signal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFireBaseInstanceIdManager extends FirebaseMessagingService {

    public static final String TOKEN_BROADCAST = "myfcmtokenbroadcast";

    private final static String TAG = "MyFirebaseId";
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        storeToken(token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);


    }

    private void storeToken(String token) {
        Log.d("check","Token storage called");
        Log.d(TAG, "Refreshed token: " + token);
        SharedPrefManager sharedPrefManager  = new SharedPrefManager(getApplicationContext());
        sharedPrefManager.storeToken(token);
    }

}
