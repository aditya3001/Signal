package com.example.signal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class Mynotificationmanager {
    private Context ctx;
    public static final int Notification_id = 0;
    public static final String CHANNEL_ID = "id";

    public Mynotificationmanager(Context ctx){
        this.ctx = ctx;
    }

    public void showNotification(String from, String notification, Intent intent){
        PendingIntent pendingIntent = PendingIntent.getActivity(
                ctx,
                Notification_id,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        );

        CharSequence textTitle;
        CharSequence textContent;
        Notification mnotification = new NotificationCompat.Builder(this.ctx, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle(from)
                .setContentText(notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH).build();
        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Notification_id,mnotification);

    }

}
