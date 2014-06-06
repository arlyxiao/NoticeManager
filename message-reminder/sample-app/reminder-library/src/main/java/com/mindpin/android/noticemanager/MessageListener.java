package com.mindpin.android.noticemanager;


import android.app.PendingIntent;

public interface MessageListener {
    public PendingIntent build_pending_intent(String message_response);
}
