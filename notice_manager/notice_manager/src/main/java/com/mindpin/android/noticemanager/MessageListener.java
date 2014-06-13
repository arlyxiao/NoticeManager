package com.mindpin.android.noticemanager;


import android.app.PendingIntent;

public interface MessageListener {
    public PendingIntent build_pending_intent(Message message_response);
}
