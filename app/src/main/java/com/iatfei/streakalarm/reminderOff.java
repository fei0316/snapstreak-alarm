package com.iatfei.streakalarm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class reminderOff extends IntentService {

    public reminderOff() {
        super("reminderOff");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationManage.CancelNotif(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("serviceEnabled", false);
        editor.apply();
    }
}
