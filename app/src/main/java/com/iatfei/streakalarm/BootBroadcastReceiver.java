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
            int count = Time.ReadNotifCount(context);
            if (count != 1)
                Time.setTally(context, (count-1));
            NotificationManage.MakeNotif(context);
        }
    }
}
