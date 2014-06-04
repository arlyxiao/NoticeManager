package com.message.noticemanager.noticelib;


import android.app.PendingIntent;

public interface MessageListener {
    public PendingIntent build_pending_intent(String message_response);
    public void build_notification(PendingIntent p_intent, String message_response);
}
