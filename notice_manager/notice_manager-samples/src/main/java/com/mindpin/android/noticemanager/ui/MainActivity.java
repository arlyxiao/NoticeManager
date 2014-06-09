package com.mindpin.android.noticemanager.ui;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;


import com.mindpin.android.noticemanager.KCMessagePushManager;
import com.mindpin.android.noticemanager.MessageListener;
import com.mindpin.android.noticemanager.R;


import org.json.JSONObject;

public class MainActivity extends Activity {
    KCMessagePushManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("启动 main activity ", "true");

        manager = new KCMessagePushManager(getApplicationContext());

        manager.set_delay(3000);
        manager.set_period(5000);

        manager.set_listen_url("http://192.168.1.101:3000");
        manager.set_notification_icon(R.drawable.ic_launcher);
        manager.add_message_listener(new MessageListener() {
            @Override
            public PendingIntent build_pending_intent(String message_response) {

                PendingIntent pending_intent =
                        MainActivity.this.build_pending_intent(message_response);


                return pending_intent;

            }

        });

        manager.start();
    }


    public PendingIntent build_pending_intent(String message_json) {
        JSONObject message_obj;
        Context context = MainActivity.this;

        try {
            if (message_json == null) {
                return null;
            }
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