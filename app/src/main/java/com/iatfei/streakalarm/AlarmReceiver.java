package com.iatfei.streakalarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

//IN USE!!!!

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long when = System.currentTimeMillis();

        initChannels(context);

        int showHours = Time.NotifTime(context);
        int notifCount = Time.ReadNotifCount(context);

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

        if (openSnap != null){
            PendingIntent pendingSnap = PendingIntent.getActivity(context, 1, openSnap, 0);
            nBuilder.addAction(R.drawable.snapchat, context.getString(R.string.menu_opensnapchat), pendingSnap);
        }

        if (showHours <= 0){
            nBuilder.setContentText(context.getString(R.string.notif_body_already));
        }
        else if (showHours < 1.8){
            nBuilder.setContentText(context.getString(R.string.notif_body_almost));
        }
        else if (notifCount == 1){
            nBuilder.setContentText(context.getString(R.string.notif_body_one, showHours));
        }
        else if (notifCount == 2){
            nBuilder.setContentText(context.getString(R.string.notif_body_two, showHours));
        }
        else {
            nBuilder.setContentText(context.getString(R.string.notif_body_multi, notifCount, showHours));
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
        notificationManager.notify(2, nBuilder.build());
        Time.NotifCountTally(context);
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
        notificationManager.createNotificationChannel(channel);
    }
}
