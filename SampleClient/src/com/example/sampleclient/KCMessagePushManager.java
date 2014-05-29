package com.example.sampleclient;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.support.v4.app.NotificationCompat;

import android.content.Context;
import android.content.Intent;
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

public class KCMessagePushManager implements MessageListener {
    public String url;
    public int period;
    public int delay;

    public void set_listen_url(String url) {
        this.url = url;
    }

    public String get_listen_url() {
        return this.url;
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

    public void add_message_listener() {

    }

    public String get_message(Context context) {
        HttpResponse response;
        try {
            String uri = get_listen_url();

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri));
            response = client.execute(request);

            String message = convert_string(response.getEntity().getContent());
            Log.i("从服务器收到的消息 ", message);

            Intent in = new Intent("app.action.new_message");
            in.putExtra("message", message);
            context.sendBroadcast(in);

        } catch (URISyntaxException e) {
            Log.i("URISyntaxException 错误", e.getMessage());
        } catch (ClientProtocolException e) {
            Log.i("ClientProtocolException 错误", e.getMessage());
        } catch (IOException e) {
            Log.i("IOException 错误", e.getMessage());
        }

        return null;
    }

    public String convert_string(InputStream inputStream) throws IOException {
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
            return "";
        }
    }

    public void build_notification(Context context, String message) {
        Notification n;

        final ComponentName receiver = new ComponentName(context, TargetActivity.class);
        Intent notice_intent = new Intent(context.getClass().getName() +
                System.currentTimeMillis());
        notice_intent.setComponent(receiver);


        // notice_intent.putExtra("notice_id", notice_id);
        notice_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, notice_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        n  = new NotificationCompat.Builder(context)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, n);

    }

    public MessageNotice build_message(Context context, String message_response) {
        get_message(context);
        return null;
    }



    public static void cancel_notice_bar(Context context, int notice_id) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (notice_id == 0) {
            notificationManager.cancelAll();
        } else {
            notificationManager.cancel(notice_id);
        }
    }
}
