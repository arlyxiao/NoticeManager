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
            String message = bundle.getString("message");

            KCMessagePushManager.notice_new_message(context, message);

        }

    }
}