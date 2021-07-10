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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;

import androidx.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    private static final long HOUR = 60 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        initChannels(context);

        long when = System.currentTimeMillis();
        long losingTime = Time.NotifTimeLong(context);
        int showHours = Time.NotifTime(context);
        int notifCount = Time.ReadNotifCount(context);

        int snoozeExtra;
        if (intent.getExtras() != null) {
            snoozeExtra = intent.getExtras().getInt("snooze");
            if (snoozeExtra == 1) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("snoozeTime", 0);
                editor.apply();
            }
        }

        if (losingTime <= (-8 * HOUR) ) {
            NotificationManage.CancelNotif(context);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("serviceEnabled", false);
            editor.apply();
        } else {
            Intent openApp = new Intent(context, MainActivity.class);
            Intent openSnap = context.getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
            Intent resetTime = new Intent(context, resetService.class);
            Intent snooze = new Intent(context, SnoozeService.class);

            PendingIntent pendingApp = PendingIntent.getActivity(context, 0, openApp, 0);
            PendingIntent pendingReset = PendingIntent.getService(context, 0, resetTime, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingSnooze = PendingIntent.getService(context, 1, snooze, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "streak")
                    .setSmallIcon(R.drawable.timer_sand)
                    .setContentTitle(context.getString(R.string.notif_title))
                    .setColor(ContextCompat.getColor(context, R.color.colorAccentDark))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .setWhen(when)
                    .setContentIntent(pendingApp);

            if (openSnap != null) {
                PendingIntent pendingSnap = PendingIntent.getActivity(context, 1, openSnap, 0);
                nBuilder.addAction(R.drawable.ic_camera_alt_black_24dp, context.getString(R.string.menu_opensnapchat), pendingSnap);
            }

            nBuilder.addAction(R.drawable.ic_done_black_24dp, context.getString(R.string.notif_sent), pendingReset);
            if (losingTime > 0)
                nBuilder.addAction(R.drawable.ic_snooze_black_24dp, context.getString(R.string.notif_snooze), pendingSnooze);

            Resources res = context.getResources();

            if (losingTime <= 0) {
                Intent turnoffNotif = new Intent(context, reminderOff.class);
                PendingIntent turnoffP = PendingIntent.getService(context, 2, turnoffNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                nBuilder.setContentText(context.getString(R.string.notif_body_already))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notif_body_already)))
                        .setContentTitle(context.getString(R.string.notif_lost_streak_title))
                        .addAction(R.drawable.ic_close_black_24dp, context.getString(R.string.notif_turnoff_reminder), turnoffP);
            } else if (losingTime < 0.9*HOUR) {
                nBuilder.setContentText(context.getString(R.string.notif_body_almost))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notif_body_almost)));
            } else if (notifCount == 1) {
                nBuilder.setContentText(convertToEnglishDigits.convert(res.getQuantityString(R.plurals.notif_body_one, showHours, showHours)))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(convertToEnglishDigits.convert(res.getQuantityString(R.plurals.notif_body_one, showHours, showHours))));
            } else if (notifCount == 2) {
                nBuilder.setContentText(convertToEnglishDigits.convert(res.getQuantityString(R.plurals.notif_body_two, showHours, showHours)))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(convertToEnglishDigits.convert(res.getQuantityString(R.plurals.notif_body_two, showHours, showHours))));
            } else {
                nBuilder.setContentText(convertToEnglishDigits.convert(res.getQuantityString(R.plurals.notif_body_multi, showHours, (notifCount - 1), showHours)))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(convertToEnglishDigits.convert(res.getQuantityString(R.plurals.notif_body_multi, showHours, (notifCount - 1), showHours))));
            }
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(notificationManager).cancel(2);
            notificationManager.notify(2, nBuilder.build());
            Time.NotifCountTally(context);
        }
    }

    private void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("streak", context.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(context.getString(R.string.channel_description));
        Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
    }
}