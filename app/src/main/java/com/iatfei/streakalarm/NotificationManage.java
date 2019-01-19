package com.iatfei.streakalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationManage extends MainActivity {

    //todo:Next Release: add postpone notification option

    public static void MakeNotif (Context c) {
        long laststreak = Time.ReadTime(c);
        long notifint = Time.LongInterval(c);
        if (laststreak == 0 || notifint == 0)
            return;
        long nextFire;
        if ((System.currentTimeMillis()-laststreak) < notifint){
            nextFire = laststreak + notifint;}
        else
            nextFire = System.currentTimeMillis();

        Intent intent1 = new Intent(c, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, nextFire, Time.LongInterval(c), pendingIntent);

        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(c, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (laststreak + 1000 * 60 * 60 * 23 - 1000 * 60 * 30), pendingIntent225);

        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(c, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (laststreak + 1000 * 60 * 60 * 24 - 1000 * 60 * 30), pendingIntent235);

        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(c, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (laststreak + 1000 * 60 * 60 * 25 - 1000 * 60 * 30), pendingIntent245);
    }
    public static void CancelNotif (Context c) {
        Intent intent1 = new Intent(c, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(c, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(c, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(c, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        am.cancel(pendingIntent225);
        am.cancel(pendingIntent235);
        am.cancel(pendingIntent245);

        NotificationManager notif =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notif.cancel(2);
        pendingIntent.cancel();
        pendingIntent225.cancel();
        pendingIntent235.cancel();
        pendingIntent245.cancel();
    }
}
