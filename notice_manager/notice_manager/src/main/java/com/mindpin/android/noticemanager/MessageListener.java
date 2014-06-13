package com.mindpin.android.noticemanager;


import android.app.PendingIntent;

import java.util.ArrayList;

public interface MessageListener {
    public PendingIntent build_pending_intent(ArrayList<Message> message_response);
}
