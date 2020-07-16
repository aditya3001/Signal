package com.example.signal;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;

public class MyFireBaseMessagingManager extends FirebaseMessagingService {
    private static final String TAG = "FCMMessage";
    public ArrayList<String> title = new ArrayList<>();
    public ArrayList<String> body = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                //scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                //handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            title.add(remoteMessage.getFrom());
            body.add(remoteMessage.getNotification().getBody());
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notifyuser(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notifyuser(String from, String notification){
        Mynotificationmanager mynotificationmanager = new Mynotificationmanager(getApplicationContext());
        mynotificationmanager.showNotification(from,notification,new Intent(getApplicationContext(),Main2Activity.class));
    }

}
