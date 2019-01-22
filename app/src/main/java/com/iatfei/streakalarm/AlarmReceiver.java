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
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long when = System.currentTimeMillis();

        initChannels(context);

        int showHours = Time.NotifTime(context);
        int notifCount = Time.ReadNotifCount(context);

        //todo:TBD if it works
        if (showHours <= -5){
            NotificationManage.CancelNotif(context);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("serviceEnabled", false);
            editor.apply();
        }
        else {
            Intent openApp = new Intent(context, MainActivity.class);
            Intent openSnap = context.getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
            Intent resetTime = new Intent(context, resetService.class);
            PendingIntent pendingApp = PendingIntent.getActivity(context, 0, openApp, 0);
            PendingIntent pendingReset = PendingIntent.getService(context, 0, resetTime, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "streak")
                    .setSmallIcon(R.drawable.timer_sand)
                    .setContentTitle(context.getString(R.string.notif_title))
                    .setColor(ContextCompat.getColor(context, R.color.colorAccentDark))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .setWhen(when)
                    .setContentIntent(pendingApp)
                    .addAction(R.drawable.ic_done_black_24dp, context.getString(R.string.notif_sent), pendingReset);

            if (openSnap != null) {
                PendingIntent pendingSnap = PendingIntent.getActivity(context, 1, openSnap, 0);
                nBuilder.addAction(R.drawable.snapchat, context.getString(R.string.menu_opensnapchat), pendingSnap);
            }

            Resources res = context.getResources();

            if (showHours <= 0) {
                Intent turnoffNotif = new Intent(context, reminderOff.class);
                PendingIntent turnoffP = PendingIntent.getService(context, 2, turnoffNotif, PendingIntent.FLAG_UPDATE_CURRENT);
                nBuilder.setContentText(context.getString(R.string.notif_body_already))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notif_body_already)))
                        .setContentTitle(context.getString(R.string.notif_lost_streak_title))
                        .addAction(R.drawable.ic_close_black_24dp, context.getString(R.string.notif_turnoff_reminder), turnoffP);
            } else if (showHours < 1.8) {
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
