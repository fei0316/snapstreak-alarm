/*
 * Copyright (c) 2017-2021 Fei Kuan.
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class NotifScheduleActivity extends AppCompatActivity {

    private static final long DAY = 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_schedule);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.notifsched_menu);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Context c = getApplicationContext();
        long now = System.currentTimeMillis();

        TextView tvh, tv1, tv2, tv3, tv4, tv6, tv7;
        // tv5

        long next, second, third, fire245, snooze;
        // fire225, fire235

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        next = settings.getLong("nextFire", 0);
        second = settings.getLong("secondFire", 0);
        third = settings.getLong("thirdFire", 0);
//        fire225 = settings.getLong("fire225", 0);
//        fire235 = settings.getLong("fire235", 0);
        fire245 = settings.getLong("fire245", 0);
        snooze = settings.getLong("snoozeTime", 0);

        tvh = findViewById(R.id.textViewHeadline);
        tv1 = findViewById(R.id.textViewContent1);
        tv2 = findViewById(R.id.textViewContent2);
        tv3 = findViewById(R.id.textViewContent3);
        tv4 = findViewById(R.id.textViewContent4);
//        tv5 = findViewById(R.id.textViewContent5);
        tv6 = findViewById(R.id.textViewContent6);
        tv7 = findViewById(R.id.textViewContent7);

        long absLastFire = Math.abs(Time.ReadTime(c) - now);
        long loseStreaksTime = (Time.ReadTime(c) + DAY) - now;
        long firstNotifTime = next - now;
        long secondNotifTime = second - now;
        long thirdNotifTime = third - now;
        long lastNotifTime = fire245 - now;
        String notifSent = getString(R.string.notifsched_notif_sent);

        if (snooze == 0)
            tvh.setText(getString(R.string.notifsched_snooze_disabled));
        else
            tvh.setText(getString(R.string.notifsched_snooze_enabled, Time.getFormatTime(snooze - now)));


        if (absLastFire > 2 * DAY)
            tv1.setText(getString(R.string.notifsched_toolong));
        else
            tv1.setText(getString(R.string.notifsched_before, Time.getTextTime(absLastFire, c)));
        if (loseStreaksTime > 0)
            tv2.setText(Time.getTextTime(loseStreaksTime, c));
        else
            tv2.setText(getString(R.string.notifsched_streaks_lost));
        if (firstNotifTime > 0)
            tv3.setText(Time.getTextTime(firstNotifTime, c));
        else
            tv3.setText(notifSent);
        if (secondNotifTime > 0)
            tv4.setText(Time.getTextTime(secondNotifTime, c));
        else
            tv4.setText(notifSent);
//        tv5.setText(Time.getTextTime(fire225 - now, c));
//        tv6.setText(Time.getTextTime(fire235 - now, c));
        if (thirdNotifTime > 0)
            tv6.setText(Time.getTextTime(thirdNotifTime, c));
        else
            tv6.setText(notifSent);
        if (lastNotifTime > 0)
            tv7.setText(Time.getTextTime(lastNotifTime, c));
        else
            tv7.setText(notifSent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notifsched_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help)  {
            AlertDialog alertDialog = new AlertDialog.Builder(NotifScheduleActivity.this).create();
            alertDialog.setTitle(getString(R.string.notifsched_help));
            alertDialog.setMessage(getString(R.string.notifsched_desc));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}