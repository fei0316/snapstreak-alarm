package com.iatfei.streakalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isEnabled = settings.getBoolean("serviceEnabled", false);

        if (isEnabled){
            //todo:Dumb attempt to "solve" an inexistent issue. Remove after some testing. Revive if I was smart...
            /*
            long interval = Time.LongInterval(context);
            long now = System.currentTimeMillis();
            long last = Time.ReadTime(context);
            if (now - last >= interval){
                Time.setTally(context, 0);
                NotificationManage.MakeNotif(context);
            } */

            Time.setTally(context, 1);
            NotificationManage.MakeNotif(context);
        }
    }
}
