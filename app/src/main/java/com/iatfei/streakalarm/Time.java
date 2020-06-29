/*
 * Copyright (c) 2017-2020 Fei Kuan.
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Time extends MainActivity {

    //todo:Implement RTL instead of disabling it

    @SuppressLint("ApplySharedPref")
    public static void ResetTime(Context c) {
        long saveLongTime = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", saveLongTime);
        editor.putInt("notifcount", 1);
        editor.commit();
    }

    public static long ReadTime(Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getLong("lastsnaptime", 123);
    }

    public static int ReadNotifCount(Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        return settings.getInt("notifcount", 1);
    }

    @SuppressLint("ApplySharedPref")
    public static void NotifCountTally(Context c) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        int count = settings.getInt("notifcount", 0);
        count++;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notifcount", count);
        editor.commit();
    }

    @SuppressLint("ApplySharedPref")
    public static void setTally(Context c, int count) {
        //Only for reboot/update to prevent excessive notifications count
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notifcount", count);
        editor.commit();
    }

    public static String ReadFormatTime(Context c) {
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

    @SuppressLint("ApplySharedPref")
    public static void SetTime(Context c, long millis) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("lastsnaptime", millis);
        editor.putInt("notifcount", 1);
        editor.commit();
    }

    public static int NotifTime(Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        long time_last = pref.getLong("lastsnaptime", 0);
        long time_to_send = (time_last + 86700000) - System.currentTimeMillis();
        double Hours = (double) (time_to_send / 1000 / 60 / 60);
        return (int) Hours;
    }

    @SuppressLint("ApplySharedPref")
    public static void SetInterval(Context c, int hours) {
        long Lhours = hours * 1000 * 60 * 60;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong("intervalLong", Lhours);
        editor.putInt("intervalInt", hours);
        editor.commit();
    }

    public static int IntInterval(Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getInt("intervalInt", 8);
    }

    public static long LongInterval(Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getLong("intervalLong", 28800000);
    }

    @SuppressLint("ApplySharedPref")
    public static void setSnooze(Context c, int halfhrs) {
        int mins = (halfhrs * 30 + 30);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("snoozeInt", mins);
        editor.commit();
    }

    public static int getSnooze(Context c) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        return pref.getInt("snoozeInt", 60);
    }

    public static String getFormatTime(Context c, long time) {
        String formatted;
        long mins, hours;
        if (TimeUnit.MILLISECONDS.toHours(time) < 0)
            hours = 0;
        else
            hours = TimeUnit.MILLISECONDS.toHours(time);

        if (TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)) < 0)
            mins = 0;
        else
            mins = TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time));

        formatted = String.format(
            Locale.ENGLISH,
            "%02d:%02d",
            hours,
            mins); //todo:not RTL/Arabic/Israeli friendly!
        return formatted;
    }

    public static String readFormatTimeNoSec(Context c) {
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
            long hours, mins;
            if (TimeUnit.MILLISECONDS.toHours(time_till) < 0)
                hours = 0;
            else
                hours = TimeUnit.MILLISECONDS.toHours(time_till);

            if (TimeUnit.MILLISECONDS.toMinutes(time_till) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_till)) < 0)
                mins = 0;
            else
                mins = TimeUnit.MILLISECONDS.toMinutes(time_till) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_till));

            formatted = String.format(
                    Locale.ENGLISH,"%02d:%02d", hours, mins); //todo:not RTL/Arabic/Israeli friendly!
        }
        return formatted;
    }

    public static String readLosingTime(Context c) {
        String formatted;
        long time_last = ReadTime(c);
        long time_to_send = (time_last + 86400000) - System.currentTimeMillis();

        long hours, mins;
        if (TimeUnit.MILLISECONDS.toHours(time_to_send) < 0)
            hours = 0;
        else
            hours = TimeUnit.MILLISECONDS.toHours(time_to_send);
        if (TimeUnit.MILLISECONDS.toMinutes(time_to_send) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_to_send)) < 0)
            mins = 0;
        else
            mins = TimeUnit.MILLISECONDS.toMinutes(time_to_send) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_to_send));

        formatted = String.format(
            Locale.ENGLISH, "%02d:%02d",hours, mins); //todo:not RTL/Arabic/Israeli friendly!
        return formatted;
    }

    /*
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        long time_last = pref.getLong("lastsnaptime", 0);
        long time_to_send = (time_last + 86700000) - System.currentTimeMillis();
        double Hours = (double) (time_to_send / 1000 / 60 / 60);
        return (int) Hours;
     */

}
