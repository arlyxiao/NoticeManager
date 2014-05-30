package com.example.sampleclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;

import org.json.JSONObject;

public class MainActivity extends Activity {
    KCMessagePushManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new KCMessagePushManager();

        manager.set_listen_url("http://192.168.1.101:3000");
        manager.set_notification_icon(R.drawable.ic_launcher);
        manager.add_message_listener(new MessageListener() {
            @Override
            public void build_notification(String message_response) {
                MainActivity.this.build_notification(message_response);
            }
        });

        manager.start(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void build_notification(String message_json) {
        Notification n;
        JSONObject message_obj;
        Context context = MainActivity.this;

        try {
            message_obj = new JSONObject(message_json);

            String title = message_obj.get("title").toString();
            String desc = message_obj.get("desc").toString();
            String other = message_obj.get("other").toString();

            Log.i("消息 title", title);
            Log.i("消息 desc", desc);
            Log.i("消息 other", other);

            final ComponentName receiver = new ComponentName(context, TargetActivity.class);
            Intent notice_intent = new Intent(context.getClass().getName() +
                    System.currentTimeMillis());
            notice_intent.setComponent(receiver);

            notice_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, notice_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            n  = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setSmallIcon(manager.get_notification_icon())
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).build();


            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, n);
        } catch (Exception e) {
            Log.i("获取 json 错误 ", e.getMessage());
        }

    }




}
