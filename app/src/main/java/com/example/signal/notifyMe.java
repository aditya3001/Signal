package com.example.signal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.Random;

import static com.example.signal.Mynotificationmanager.Notification_id;

public class notifyMe {
    private Context ctx;
    public notifyMe(Context ctx) {
        this.ctx = ctx;
    }

    void showNotification(String title, String message) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        Notification_id = m;
        Log.d("Note "," NOTIFY "+Notification_id);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx.getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false); // clear notification after click
        Intent intent = new Intent(ctx.getApplicationContext(), Main5Activity.class);
        PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(Notification_id, mBuilder.build());
    }
}
