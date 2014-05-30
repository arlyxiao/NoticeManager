package com.example.sampleclient;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MessageNoticeService extends Service {
    KCMessagePushManager manager;

    @Override
    public void onCreate() {
    }

    public class LocalBinder extends Binder {
        public MessageNoticeService getService() {
            return MessageNoticeService.this;
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("服务开始启动了 ", "true");

//        int delay = 3000;
//        int period = 1000;
//
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {
//
//            }
//        }, delay, period);


        return START_REDELIVER_INTENT;
    }

    public void show_notice(final KCMessagePushManager manager) {
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

                manager.message_listener.build_notification(message_response);
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
