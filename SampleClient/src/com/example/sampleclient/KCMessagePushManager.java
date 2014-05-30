package com.example.sampleclient;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class KCMessagePushManager implements MessageListener {
    public String url;
    public int period;
    public int delay;
    public int notification_icon;

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

    public void set_notification_icon(int notification_icon) {
        this.notification_icon = notification_icon;
    }

    public int get_notification_icon() {
        return this.notification_icon;
    }



    public void add_message_listener() {

    }

    public String get_message(Context context) {
//        String img_url;
//        String title;
//        String desc;
//        String other;

        HttpResponse response;
        try {
            String uri = get_listen_url();

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(uri));
            response = client.execute(request);

            String message_json = convert_string(response.getEntity().getContent());
            Log.i("从服务器收到的消息 ", message_json);

            try {
//                JsonParser parser = new JsonParser();
//                JsonObject message_data;

//                message_data = parser.parse(message_json).getAsJsonObject();
//                img_url = message_data.get("img_url").getAsString();
//                title = message_data.get("title").toString();
//                desc = message_data.get("desc").toString();
//                other = message_data.get("other").toString();

//                Log.i("消息 img_url", img_url);
//                Log.i("消息 title", title);
//                Log.i("消息 desc", desc);
//                Log.i("消息 other", other);

                Intent in = new Intent("app.action.new_message");
                in.putExtra("message_json", message_json);
//                in.putExtra("img_url", img_url);
//                in.putExtra("title", title);
//                in.putExtra("desc", desc);
//                in.putExtra("other", other);
                context.sendBroadcast(in);

            } catch (Exception e) {
                e.printStackTrace();
            }

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

    public void build_notification(Context context, String message_json) {
        Notification n;
        JSONObject message_obj;

        KCMessagePushManager manager = new KCMessagePushManager();

        try {
            message_obj = new JSONObject(message_json);

            String img_url = message_obj.get("img_url").toString();
            String title = message_obj.get("title").toString();
            String desc = message_obj.get("desc").toString();
            String other = message_obj.get("other").toString();

            Log.i("消息 img_url", img_url);
            Log.i("消息 title", title);
            Log.i("消息 desc", desc);
            Log.i("消息 other", other);

            final ComponentName receiver = new ComponentName(context, TargetActivity.class);
            Intent notice_intent = new Intent(context.getClass().getName() +
                    System.currentTimeMillis());
            notice_intent.setComponent(receiver);


            // notice_intent.putExtra("notice_id", notice_id);
            notice_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, notice_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            n  = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setSmallIcon(manager.get_notification_icon())
                    .setContentIntent(pIntent)
                    .setAutoCancel(true).build();


            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            notificationManager.notify(1, n);
        } catch (Exception e) {
            Log.i("获取 json 错误 ", e.getMessage());
        }

    }

    public MessageNotice build_message(Context context, String message_response) {
        get_message(context);
        return null;
    }

//    public void download_img(Context context, String download_url) {
//        // String dir = Environment.getExternalStorageDirectory().getPath() + "/sample-notice/";
//        ContextWrapper c = new ContextWrapper(context);
//        String dir = c.getFilesDir().getPath() + "/res/notice";
//        Log.i("下载的目录 ", dir);
//
//        File f = new File(dir);
//        if (f.isDirectory()) {
//            Log.i("目录已经存在 ", dir);
//        } else {
//            boolean success = f.mkdir();
//            if (!success) {
//                Log.i("目录文件没有创建成功 ", "true");
//            }
//        }
//
//        Log.i("要下载的URL ", download_url);
//
//        try {
//            URL url = new URL(download_url);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//
//            String data1 = String.valueOf(String.format(dir + "%d.jpg", System.currentTimeMillis()));
//
//            FileOutputStream stream = new FileOutputStream(data1);
//
//            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
//            myBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outstream);
//            byte[] byteArray = outstream.toByteArray();
//
//            stream.write(byteArray);
//            stream.close();
//
//            Log.i("服务器 img 下载成功 ", "true");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("下载错误 ", e.getMessage());
//        }
//    }



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
