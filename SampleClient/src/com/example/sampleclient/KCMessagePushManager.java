package com.example.sampleclient;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.support.v4.app.NotificationCompat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KCMessagePushManager {

    public static void notice_new_message(Context context, String message) {
        Notification n;

        final ComponentName receiver = new ComponentName(context, TargetActivity.class);
        Intent notice_intent = new Intent(context.getClass().getName() +
                System.currentTimeMillis());
        notice_intent.setComponent(receiver);


        // notice_intent.putExtra("notice_id", notice_id);
        notice_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, notice_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        n  = new NotificationCompat.Builder(context)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, n);

    }

    public static void cancel_notice_bar(Context context, int notice_id) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (notice_id == 0) {
            notificationManager.cancelAll();
        } else {
            notificationManager.cancel(notice_id);
        }
    }
}
