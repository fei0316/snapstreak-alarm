/*
 * Copyright (c) 2017-2021 Fei Kuan.
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
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;

import java.util.Objects;

public class NotificationManage extends MainActivity {

    private static final long DAY = 24 * 60 * 60 * 1000;
    private static final long HOUR = 60 * 60 * 1000;

    public static void MakeNotif (Context c) {
        long laststreak = Time.ReadTime(c);
        long notifint = Time.LongInterval(c);
        if (laststreak == 0 || notifint == 0)
            return;
        long nextFire, secondFire, thirdFire, fire245, cancelFire;
        //fire225, fire235,

        long now = System.currentTimeMillis();
        if ((now - laststreak) < notifint) {
            nextFire = laststreak + notifint;
            secondFire = laststreak + notifint + ( (DAY - notifint) / 2);
            thirdFire = laststreak + notifint + ( ((DAY - notifint) / 4) * 3);
        }
        else {
            nextFire = now;
            secondFire = now + (( DAY + laststreak - now) / 2);
            thirdFire = now + (( DAY + laststreak - now) / 4) * 3;
        }

//        fire225 = (long) (laststreak + HOUR * 22.5);
//        fire235 = (long) (laststreak + HOUR * 23.5);
        fire245 = (long) (laststreak + HOUR * 24.5);
        cancelFire = (laststreak + HOUR * 36);

        /*
        nextFire is the first notification shown to user. Usually x hours after streak sent time. nextFire = currentTimeMillis() when the notification should already be fired when MakeNotif was called (e.g. when booting after original notification is missed).
        secondFire is the second notification shown to user. It is halfway between the first notification and the time streaks will be lost (24hours + streak sent time)
        thirdFire is the third notification shown to user. It is 3 quarters of the time between the first notification and the time streaks will be lost.
        fire225/235/245 are the third/fourth/fifth notifications shown. They are 22.5/23.5/24.5 hours after streak sent time. They are meant to be last-minute warnings.
        NOTE: fire225 and fire235 are not used anymore.
        Content of the notifications are determined in AlarmReceiver depending on the time left and number of notifications sent, not which AlarmManager triggered it.
         */

        Intent intent1 = new Intent(c, AlarmReceiver.class);
        intent1.putExtra("snooze", 0);
        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(c, 4, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentThird = PendingIntent.getBroadcast(c, 6, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(c, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(c, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(c, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(c, 7, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            am.set(AlarmManager.RTC_WAKEUP, nextFire, pendingIntent);
            am.set(AlarmManager.RTC_WAKEUP, secondFire, pendingIntentSecond);
            am.set(AlarmManager.RTC_WAKEUP, thirdFire, pendingIntentThird);
//            am.set(AlarmManager.RTC_WAKEUP, fire225, pendingIntent225);
//            am.set(AlarmManager.RTC_WAKEUP, fire235, pendingIntent235);
            am.set(AlarmManager.RTC_WAKEUP, fire245, pendingIntent245);
            am.set(AlarmManager.RTC_WAKEUP, cancelFire, pendingIntentCancel);
        } else {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextFire, pendingIntent);
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, secondFire, pendingIntentSecond);
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, thirdFire, pendingIntentThird);
//            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, fire225, pendingIntent225);
//            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, fire235, pendingIntent235);
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, fire245, pendingIntent245);
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cancelFire, pendingIntentCancel);
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("nextFire", nextFire);
        editor.putLong("secondFire", secondFire);
        editor.putLong("thirdFire", thirdFire);
//        editor.putLong("fire225", fire225);
//        editor.putLong("fire235", fire235);
        editor.putLong("fire245", fire245);
        editor.apply();
    }

    public static void Snooze (Context c) {
        Intent intent = new Intent(c, AlarmReceiver.class);
        intent.putExtra("snooze", 1);

        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        int snoozeduration = Time.getSnooze(c);
        PendingIntent pendingSnooze = PendingIntent.getBroadcast(c, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long snoozeTime = (System.currentTimeMillis() + 1000 * 60 * snoozeduration);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            am.set(AlarmManager.RTC_WAKEUP, snoozeTime, pendingSnooze);
        } else {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, snoozeTime, pendingSnooze);
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("snoozeTime", snoozeTime);
        editor.apply();
    }

    public static void CloseNotif (Context c) {
        NotificationManager notif =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notif.cancel(2);
    }

    public static void CancelNotif (Context c) {
        Intent intent1 = new Intent(c, AlarmReceiver.class);
        Intent intent = new Intent(c, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentSecond = PendingIntent.getBroadcast(c, 4, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentThird = PendingIntent.getBroadcast(c, 6, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent225 = PendingIntent.getBroadcast(c, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent pendingIntent235 = PendingIntent.getBroadcast(c, 2, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent245 = PendingIntent.getBroadcast(c, 3, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(c, 7, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingSnooze = PendingIntent.getBroadcast(c, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        AlarmManager am = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        am.cancel(pendingIntentSecond);
        am.cancel(pendingIntentThird);
//        am.cancel(pendingIntent225);
//        am.cancel(pendingIntent235);
        am.cancel(pendingIntent245);
        am.cancel(pendingIntentCancel);
        am.cancel(pendingSnooze);

        NotificationManager notif =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notif).cancel(2);
        pendingIntent.cancel();
        pendingIntentSecond.cancel();
        pendingIntentThird.cancel();
//        pendingIntent225.cancel();
//        pendingIntent235.cancel();
        pendingIntent245.cancel();
        pendingIntentCancel.cancel();
        pendingSnooze.cancel();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("nextFire", 0);
        editor.putLong("secondFire", 0);
        editor.putLong("thirdFire", 0);
//        editor.remove("fire225");
//        editor.remove("fire235");
//        editor.putLong("fire225", 0);
//        editor.putLong("fire235", 0);
        editor.putLong("fire245", 0);
        editor.putLong("snoozeTime", 0);
        editor.apply();
    }
}