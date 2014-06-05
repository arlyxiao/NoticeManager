package com.message.mylib;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMessageNoticeServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent intent_service = new Intent(context, MessageNoticeService.class);
            context.startService(intent_service);
        }
    }
}