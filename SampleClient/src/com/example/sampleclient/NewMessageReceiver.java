package com.example.sampleclient;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NewMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i("接收消息提示 broadcast receiver ", "true");

        String action = intent.getAction();

        if(action.equals("app.action.new_message")){
            Log.i("新消息提示通知正常运行 ", "true");

            Bundle bundle = intent.getExtras();
//            String img_url = bundle.getString("img_url");
//            String title = bundle.getString("title");
//            String desc = bundle.getString("desc");
//            String other = bundle.getString("other");
            String message_json = bundle.getString("message_json");

            KCMessagePushManager manager = new KCMessagePushManager();
            manager.build_notification(context, message_json);

        }

    }
}