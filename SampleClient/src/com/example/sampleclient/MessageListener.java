package com.example.sampleclient;


import android.content.Context;

public interface MessageListener {
    public MessageNotice build_message(Context context, String message_response);

    public void build_notification(Context context, String message);
}
