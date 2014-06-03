package com.example.sampleclient;

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

        manager = new KCMessagePushManager(MainActivity.this);

        manager.set_listen_url("http://192.168.1.100:3000");
        manager.set_notification_icon(R.drawable.ic_launcher);
        manager.add_message_listener(new MessageListener() {
            @Override
            public PendingIntent build_pending_intent(String message_response) {

                PendingIntent pending_intent =
                        MainActivity.this.build_notification(message_response);


                return pending_intent;

            }

            @Override
            public void build_notification(PendingIntent p_intent, String message_response) {
                try {
                    Context context = getApplicationContext();
                    JSONObject message_obj = new JSONObject(message_response);

                    String title = message_obj.get("title").toString();
                    String desc = message_obj.get("desc").toString();
                    String other = message_obj.get("other").toString();

                    Log.i("消息 title", title);
                    Log.i("消息 desc", desc);
                    Log.i("消息 other", other);

                    android.app.Notification n =
                            new NotificationCompat.Builder(context)
                                    .setContentTitle(title)
                                    .setContentText(desc)
                                    .setSmallIcon(manager.get_notification_icon())
                                    .setContentIntent(p_intent)
                                    .setAutoCancel(true).build();


                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                    notificationManager.notify(1, n);

                } catch (Exception e) {
                    Log.i("获取 json 错误 ", e.getMessage());
                }
            }
        });

        manager.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public PendingIntent build_notification(String message_json) {
        JSONObject message_obj;
        Context context = MainActivity.this;

        try {
            message_obj = new JSONObject(message_json);


            final ComponentName receiver = new ComponentName(context, TargetActivity.class);
            Intent notice_intent = new Intent(context.getClass().getName() +
                    System.currentTimeMillis());
            notice_intent.setComponent(receiver);

            notice_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent p_intent = PendingIntent.getActivity(context, 0, notice_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            return p_intent;
        } catch (Exception e) {
            Log.i("获取 json 错误 ", e.getMessage());
        }
        return null;
    }




}
