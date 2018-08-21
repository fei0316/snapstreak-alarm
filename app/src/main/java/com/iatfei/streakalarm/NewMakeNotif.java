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

public class NewMakeNotif extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long millis_now = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        long time_last = pref.getLong("lastsnaptime", 0);
        long time_to_send = (time_last + 86400000) - millis_now;
        long longHours = (time_to_send / 1000 / 60 / 60);
        int showHours = (int) longHours;

        Intent openApp = new Intent(context, MainActivity.class);
        Intent openSnap = context.getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
        //Intent resetTime = new Intent(context, resetService.class);
        //resetTime.setAction(resetService.ACTION1);
        PendingIntent pendingApp = PendingIntent.getActivity(context, 0, openApp, 0);
        //PendingIntent pendingReset = PendingIntent.getService(context, 154, resetTime, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "1")
                .setSmallIcon(R.drawable.ic_close_black_24dp) //for now
                .setContentTitle(Resources.getSystem().getString(R.string.notif_title))
                .setContentText(Resources.getSystem().getString(R.string.notif_body, showHours))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingApp);
        //.addAction(R.drawable.ic_stat_name, getString(R.string.just_now), pendingReset);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (openSnap != null) {
            openSnap.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingSnap = PendingIntent.getActivity(context, 0, openSnap, 0);
            nBuilder.addAction(R.drawable.ic_close_black_24dp, Resources.getSystem().getString(R.string.menu_opensnapchat), pendingSnap); //icon temp.
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Resources.getSystem().getString(R.string.channel_name);
            String description = Resources.getSystem().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, nBuilder.build());
    }
}
