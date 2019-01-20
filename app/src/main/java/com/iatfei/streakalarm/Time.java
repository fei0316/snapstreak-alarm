package com.iatfei.streakalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Time extends MainActivity{

    //todo:Implement RTL instead of disabling it

    public static void ResetTime (Context c) {
        long saveLongTime = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", saveLongTime);
        editor.putInt("notifcount",1);
        editor.apply();
    }
    public static long ReadTime (Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getLong("lastsnaptime", 0);
    }

    public static int ReadNotifCount (Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getInt("notifcount", 1);
    }

    public static void NotifCountTally (Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        int count = settings.getInt("notifcount", 0);
        count++;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notifcount", count);
        editor.apply();
    }

    public static void resetTally (Context c) {
        //Only for reboot/update to prevent excessive notifications count
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notifcount", 0);
        editor.apply();
    }

    public static String ReadFormatTime (Context c) {
        String formatted;
        long time_last = ReadTime(c);
        long time_till = System.currentTimeMillis() - time_last;
        if (time_till > 604800000)
            formatted = c.getResources().getString(R.string.main_unknown);
        else if (time_till < 0)
            formatted = c.getResources().getString(R.string.main_reset_time);
        else {
            formatted = String.format(
                    Locale.ENGLISH,
                    "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(time_till),
                    TimeUnit.MILLISECONDS.toMinutes(time_till) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_till)),
                    TimeUnit.MILLISECONDS.toSeconds(time_till) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_till))); //todo:not RTL/Arabic/Israeli friendly!
        }
        return formatted;
    }
    public static void SetTime (Context c, long millis) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", millis);
        editor.putInt("notifcount",1);
        editor.apply();
    }

    public static int NotifTime (Context c){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        long time_last = pref.getLong("lastsnaptime", 0);
        long time_to_send = (time_last + 86400000) - System.currentTimeMillis();
        long longHours = (time_to_send / 1000 / 60 / 60);
        return (int) Math.floor(longHours);
    }

    public static void SetInterval (Context c, int hours){
        long Lhours = hours * 1000 * 60 * 60;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("intervalLong", Lhours);
        editor.putInt("intervalInt", hours);
        editor.apply();
    }

    public static int IntInterval (Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getInt("intervalInt", 8);
    }

    public static long LongInterval (Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getLong("intervalLong", 28800000);
    }


    //may not need anymore...

    /*public static void setLastFire (Context c, long millis){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("nextnotif", millis);
        editor.apply();
    }

    public static long getLastFire (Context c){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        long l = pref.getLong("nextnotif", 0);
        return l;
    }*/
}
