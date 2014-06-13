package com.mindpin.android.noticemanager;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class KCMessagePushManager {
    Context context;
    public MessageListener message_listener;
    public String url;
    public int period;
    public int delay;
    public int notification_icon;

    public KCMessagePushManager(Context context) {
        this.context = context;
    }

    public void set_listen_url(String url) {
        this.url = url;
    }

    public String get_listen_url() {
        String server_url = this.url + "?token=" + generate_token();
        Log.i("服务器URL  ", server_url);
        return server_url;
    }

    public void set_period(int period) {
        this.period = period;
    }

    public int get_period() {
        return this.period;
    }

    public void set_delay(int delay) {
        this.delay = delay;
    }

    public int get_delay() {
        return this.delay;
    }

    public void set_notification_icon(int notification_icon) {
        this.notification_icon = notification_icon;
    }

    public int get_notification_icon() {
        return this.notification_icon;
    }


    public void add_message_listener(MessageListener message_listener) {
        this.message_listener = message_listener;
    }

    public void build_notification(PendingIntent p_intent, String message_response) {
        try {
            if (message_response == null) {
                return;
            }

            JSONObject message_obj = new JSONObject(message_response);

            Boolean has_unread = message_obj.getBoolean("has_unread");
            if (!has_unread) {
                Log.i("暂时没有新消息 ", "true");
                return;
            }
            String messages = message_obj.getString("messages").toString();
            Log.i("新消息 ", messages);
            JSONArray messages_obj = message_obj.getJSONArray("messages");

            for (int i = 0; i < messages_obj.length(); i++) {

                message_obj = messages_obj.getJSONObject(i);

                String title = message_obj.get("title").toString();
                String desc = message_obj.get("desc").toString();
                String other = message_obj.get("other").toString();

                Log.i("消息 title", title);
                Log.i("消息 desc", desc);
                Log.i("消息 other", other);

                android.app.Notification notice =
                        new NotificationCompat.Builder(context)
                                .setContentTitle(title)
                                .setContentText(desc)
                                .setSmallIcon(get_notification_icon())
                                .setContentIntent(p_intent)
                                .setAutoCancel(true).getNotification();


                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);



                Random rand = new Random();

                int id = rand.nextInt(999999999);
                Log.i("通知 id ", Integer.toString(id));

                notificationManager.notify(id, notice);

            }


        } catch (Exception e) {
            Log.i("获取 json 错误 ", e.getMessage());
        }
    }

    public String get_message() {

        HttpResponse response;
        try {
            String uri = get_listen_url();

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri));
            response = client.execute(request);

            String message_json = convert_string(response.getEntity().getContent());

            Log.i("从服务器收到的消息 ", message_json);

            if (!is_json_valid(message_json)) {
                return null;
            }

            return message_json;

        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.i("URISyntaxException 错误", e.getMessage());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.i("ClientProtocolException 错误", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("IOException 错误", e.getMessage());
        }

        return null;
    }

    private boolean is_json_valid(String test) {
        try {
            new JSONObject(test);
        } catch(JSONException ex) {
            try {
                new JSONArray(test);
            }
            catch(JSONException e) {
                return false;
            }
        }
        return true;
    }

    private String convert_string(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            return null;
        }
    }

    private String generate_token() {
        String token;

        try {

            SharedPreferences sp0 = context.getSharedPreferences("KCMessagePushManager", 0);
            token = sp0.getString("customer_token", null);

            Log.i("获取到的 token 值", token);

            return token;

        } catch (Exception e) {
            Log.i("token 生成错误 ", e.getMessage());

            token = UUID.randomUUID().toString();

            SharedPreferences sp = context.getSharedPreferences("KCMessagePushManager", 0);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("customer_token", token);
            Ed.commit();

            Log.i("新生成的 token 值", token);
        }

        return token;
    }


    public void start() {

        int delay = get_delay();
        int period = get_period();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Intent intent_service = new Intent(context, MessageNoticeService.class);
                context.startService(intent_service);
                context.bindService(intent_service, mConnection, Context.BIND_AUTO_CREATE);
            }
        }, delay, period);
    }

    MessageNoticeService m_service;
    boolean m_bound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MessageNoticeService.LocalBinder binder = (MessageNoticeService.LocalBinder) service;
            m_service = binder.getService();
            m_bound = true;

            m_service.show_notice(KCMessagePushManager.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            m_bound = false;
        }
    };

}
