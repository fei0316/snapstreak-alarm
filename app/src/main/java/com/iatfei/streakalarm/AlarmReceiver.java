package com.iatfei.streakalarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        long when = System.currentTimeMillis();

        initChannels(context);

        int showHours = Time.NotifTime(context);

        Intent openApp = new Intent(context, MainActivity.class);
        Intent openSnap = context.getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
        //Intent resetTime = new Intent(context, resetService.class);
        //resetTime.setAction(resetService.ACTION1);
        PendingIntent pendingApp = PendingIntent.getActivity(context, 0, openApp, 0);
        //PendingIntent pendingReset = PendingIntent.getService(context, 154, resetTime, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "streak")
                .setSmallIcon(R.drawable.ic_close_black_24dp) //for now
                .setContentTitle(context.getString(R.string.notif_title))
                .setContentText(context.getString(R.string.notif_body, showHours))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setWhen(when)
                .setContentIntent(pendingApp);
        if (openSnap != null){
            PendingIntent pendingSnap = PendingIntent.getActivity(context, 1, openSnap, 0);
            nBuilder.addAction(R.drawable.ic_close_black_24dp, "opensnapchat", pendingSnap);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, nBuilder.build());
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("streak", context.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(context.getString(R.string.channel_description));
        notificationManager.createNotificationChannel(channel);
    }
}
