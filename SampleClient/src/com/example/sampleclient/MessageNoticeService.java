package com.example.sampleclient;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                // manager.get_message();
            }
        });

        thread.start();

        return START_REDELIVER_INTENT;
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
