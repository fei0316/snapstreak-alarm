/*
 * Copyright (c) 2017-2019 Fei Kuan.
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
        return settings.getLong("lastsnaptime", 123);
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

    public static void setTally (Context c, int count) {
        //Only for reboot/update to prevent excessive notifications count
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notifcount", count);
        editor.apply();
    }

    public static String ReadFormatTime (Context c) {
        String formatted;
        long time_last = ReadTime(c);
        long time_till = System.currentTimeMillis() - time_last;
        if (time_last == 2)
            formatted = c.getResources().getString(R.string.main_new);
        else if (time_till > 172800000)
            formatted = c.getResources().getString(R.string.main_long_ago);
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
        long time_to_send = (time_last + 86700000) - System.currentTimeMillis();
        double Hours = (double) (time_to_send / 1000 / 60 / 60);
        return (int) Hours;
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
