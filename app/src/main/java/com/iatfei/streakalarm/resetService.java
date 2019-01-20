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
        NotificationManager notif =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notif).cancel(2);

        Intent intent1 = new Intent(getApplicationContext(), AlarmReceiver.class);
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(this, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        Objects.requireNonNull(am).set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + 1000 * 60 * 60 * 23 - 1000 * 60 * 30), pendingIntent225);

        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(this, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + 1000 * 60 * 60 * 24 - 1000 * 60 * 30), pendingIntent235);

        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(this, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + 1000 * 60 * 60 * 25 - 1000 * 60 * 30), pendingIntent245);
    }
}
