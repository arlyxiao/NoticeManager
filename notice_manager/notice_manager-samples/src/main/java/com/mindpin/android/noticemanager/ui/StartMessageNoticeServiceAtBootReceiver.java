package com.mindpin.android.noticemanager.ui;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mindpin.android.noticemanager.KCMessagePushManager;
import com.mindpin.android.noticemanager.Message;
import com.mindpin.android.noticemanager.MessageListener;

import com.mindpin.android.noticemanager.R;


public class StartMessageNoticeServiceAtBootReceiver extends BroadcastReceiver {
    KCMessagePushManager manager;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Intent intent_service = new Intent(context, MessageNoticeService.class);
//            context.startService(intent_service);
        }

        manager = new KCMessagePushManager(context);

        manager.set_delay(3000);
        manager.set_period(5000);

        manager.set_listen_url("http://192.168.1.100:3000");
        manager.set_notification_icon(R.drawable.ic_launcher);
        manager.add_message_listener(new MessageListener() {
            @Override
            public PendingIntent build_pending_intent(Message message_response) {

                PendingIntent pending_intent =
                        StartMessageNoticeServiceAtBootReceiver.
                                this.build_pending_intent(context, message_response);

                return pending_intent;
            }

        });

        manager.start();
    }

    public PendingIntent build_pending_intent(Context context, Message message_response) {
        try {
            if (message_response == null) {
                return null;
            }

            // 取得 message_response 内容构建需要的消息, other: 根据需要来改
            String other = message_response.other;

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