package com.mindpin.android.noticemanager;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MessageNoticeService extends Service {
    KCMessagePushManager manager;

    @Override
    public void onCreate() {
        Log.i("只是测试 ", "true");
    }

    public class LocalBinder extends Binder {
        public MessageNoticeService getService() {
            return MessageNoticeService.this;
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("服务开始启动了 ", "true");

        if (manager != null) {
            show_notice(manager);
        }

        return START_REDELIVER_INTENT;
    }

    public void show_notice(final KCMessagePushManager manager) {
        this.manager = manager;

        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... objects) {
                return manager.get_message();
            }

            @Override
            protected void onPostExecute(String message_response) {
                super.onPostExecute(message_response);

                if (message_response == null) {
                    Log.i("无法从服务器获取消息 ", "true");
                    return;
                }

                PendingIntent p_intent = manager.message_listener
                        .build_pending_intent(message_response);

                manager.build_notification(p_intent, message_response);
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return m_binder;
    }

    private final IBinder m_binder = new LocalBinder();
}
