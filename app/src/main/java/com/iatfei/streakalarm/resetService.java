package com.iatfei.streakalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class resetService extends IntentService {

    public resetService() {
        super("resetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time.ResetTime(getApplicationContext());
        NotificationManage.CancelNotif(getApplicationContext());
        NotificationManage.MakeNotif(getApplicationContext());
    }
}
