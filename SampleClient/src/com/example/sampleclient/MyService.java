package com.example.sampleclient;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyService  extends Service {
    KCMessagePushManager manager;

    @Override
    public void onCreate() {
        manager.set_listen_url("http://192.168.1.100:3000");
    }

    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.i("服务开始启动了 ", "true");

        int delay = 3000;
        int period = 1000;

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {
//
//            }
//        }, delay, period);


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                manager.get_message(getApplicationContext());
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
