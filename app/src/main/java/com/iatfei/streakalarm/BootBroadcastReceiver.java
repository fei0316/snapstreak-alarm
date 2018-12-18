package com.iatfei.streakalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ReadService.status(context)) {
            long lastnotif = Time.getLastFire(context);
            long notifint = Time.LongInterval(context);
            long nextFire;
            if ((System.currentTimeMillis() - lastnotif) < 82800000) {
                if ((System.currentTimeMillis() - lastnotif) < notifint) {
                    nextFire = notifint - (System.currentTimeMillis() - lastnotif);
                } else
                    nextFire = (10 * 60 * 1000);

                Intent intent1 = new Intent(context, AlarmReceiver.class);
                AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am.setRepeating(AlarmManager.RTC_WAKEUP, nextFire, Time.LongInterval(context), pendingIntent);

                PendingIntent pendingIntent225 = PendingIntent.getBroadcast(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, (lastnotif + 1000 * 60 * 60 * 23 - 1000 * 60 * 30), pendingIntent225);

                PendingIntent pendingIntent235 = PendingIntent.getBroadcast(context, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, (lastnotif + 1000 * 60 * 60 * 24 - 1000 * 60 * 30), pendingIntent235);

                PendingIntent pendingIntent245 = PendingIntent.getBroadcast(context, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, (lastnotif + 1000 * 60 * 60 * 25 - 1000 * 60 * 30), pendingIntent245);
            }
            else {
                //todo:make notification about lost streak.
            }
        }
    }
}
