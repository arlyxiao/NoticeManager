package com.mindpin.android.noticemanager;


import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MessageNoticeService extends Service {
    KCMessagePushManager manager;

    @Override
    public void onCreate() {
        Log.i("开始运行 service create ", "true");
    }

    public class LocalBinder extends Binder {
        public MessageNoticeService getService() {
            return MessageNoticeService.this;
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("服务开始启动了 ", "true");


//        int delay = manager.get_delay();
//        int period = manager.get_period();
//
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {
//                if (manager != null) {
//                    show_notice(manager);
//                }
//
//            }
//        }, delay, period);

//        SharedPreferences mPrefs = getSharedPreferences("manager", 0);
//        Gson gson = new Gson();
//        String shared = mPrefs.getString("shared", "");
//        manager = gson.fromJson(shared, KCMessagePushManager.class);

        manager = intent.getParcelableExtra("manager_obj");

        if (manager != null) {
            Log.i("判断是否有 manager ", "true");
            show_notice(manager);
        }


        return START_REDELIVER_INTENT;
    }

    public void show_notice(final KCMessagePushManager manager) {
        this.manager = manager;

        new AsyncTask<Void, Void, ArrayList<Message>>() {

            @Override
            protected ArrayList<Message> doInBackground(Void... objects) {
                return manager.get_message_list();
            }

            @Override
            protected void onPostExecute(ArrayList<Message> message_list) {
                super.onPostExecute(message_list);

                if (message_list == null) {
                    Log.i("无法从服务器获取消息 ", "true");
                    return;
                }

                for (int i = 0; i < message_list.size(); i++) {
                    Message message_response = message_list.get(i);
                    PendingIntent p_intent = manager.message_listener
                            .build_pending_intent(message_response);

                    manager.build_notification(p_intent, message_response);
                }

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
