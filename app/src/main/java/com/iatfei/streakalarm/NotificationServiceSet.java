package com.iatfei.streakalarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

public class NotificationServiceSet extends IntentService {
    public NotificationServiceSet() {
        super("NotificationServiceSet");
    }
//todo:not currently in use.
    @Override
    protected void onHandleIntent(Intent intent) {
        //NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //manager.cancel(1);
        long trigger_time = System.currentTimeMillis() + 120000; //todo: add option to select frequency of reminder
        Intent alarmInt = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmInt, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, trigger_time, pi);
    }

}