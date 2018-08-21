package com.iatfei.streakalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.concurrent.TimeUnit;

public class Time extends MainActivity {
    public static void ResetTime (Context c) {
        long saveLongTime = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", saveLongTime);
        editor.apply();
    }
    public static long ReadTime (Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        long time_last = settings.getLong("lastsnaptime", 0);
        return time_last;
    }
    public static String ReadFormatTime (Context c) {
        long time_last = ReadTime(c);
        long time_till = System.currentTimeMillis() - time_last;
        String formatted = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time_till),
                TimeUnit.MILLISECONDS.toMinutes(time_till) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_till)),
                TimeUnit.MILLISECONDS.toSeconds(time_till) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_till)));
        return formatted;
    }
    public static void SetTime (Context c, long millis) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", millis);
        editor.apply();
    }

}
