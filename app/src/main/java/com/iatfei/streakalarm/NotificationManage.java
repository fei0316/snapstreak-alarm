/*
 * Copyright (c) 2017-2020 Fei Kuan.
 *
 * This file is part of Streak Alarm
 * (see <https://github.com/fei0316/snapstreak-alarm>).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.iatfei.streakalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class NotificationManage extends MainActivity {

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
        Objects.requireNonNull(am).setRepeating(AlarmManager.RTC_WAKEUP, nextFire, Time.LongInterval(c), pendingIntent);

        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(c, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (laststreak + 1000 * 60 * 60 * 23 - 1000 * 60 * 30), pendingIntent225);

        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(c, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (laststreak + 1000 * 60 * 60 * 24 - 1000 * 60 * 30), pendingIntent235);

        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(c, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (laststreak + 1000 * 60 * 60 * 25 - 1000 * 60 * 30), pendingIntent245);
    }

    public static void Snooze (Context c) {
        Intent intent = new Intent(c, AlarmReceiver.class);
        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        int snoozeduration = Time.getSnooze(c);
        PendingIntent pendingSnooze = PendingIntent.getBroadcast(c, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + 1000 * 60 * snoozeduration), pendingSnooze);
    }

    public static void CloseNotif (Context c) {
        NotificationManager notif =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notif.cancel(2);
    }

    public static void CancelNotif (Context c) {
        Intent intent1 = new Intent(c, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(c, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(c, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(c, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        Objects.requireNonNull(am).cancel(pendingIntent);
        am.cancel(pendingIntent225);
        am.cancel(pendingIntent235);
        am.cancel(pendingIntent245);

        NotificationManager notif =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notif).cancel(2);
        pendingIntent.cancel();
        pendingIntent225.cancel();
        pendingIntent235.cancel();
        pendingIntent245.cancel();
    }
}
