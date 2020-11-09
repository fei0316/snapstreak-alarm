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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class NotifScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_schedule);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        Context c = getApplicationContext();
        long now = System.currentTimeMillis();

        TextView tvh, tv1, tv2, tv3, tv4, tv5, tv6, tv7;

        long next, second, fire225, fire235, fire245, snooze;

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(c);
        next = settings.getLong("nextFire", 0);
        second = settings.getLong("secondFire", 0);
        fire225 = settings.getLong("fire225", 0);
        fire235 = settings.getLong("fire235", 0);
        fire245 = settings.getLong("fire245", 0);
        snooze = settings.getLong("snoozeTime", 0);

        tvh = findViewById(R.id.textViewHeadline);
        tv1 = findViewById(R.id.textViewContent1);
        tv2 = findViewById(R.id.textViewContent2);
        tv3 = findViewById(R.id.textViewContent3);
        tv4 = findViewById(R.id.textViewContent4);
        tv5 = findViewById(R.id.textViewContent5);
        tv6 = findViewById(R.id.textViewContent6);
        tv7 = findViewById(R.id.textViewContent7);

        if (snooze == 0)
            tvh.setText(getString(R.string.notifsched_snooze_disabled));
        else
            tvh.setText(getString(R.string.notifsched_snooze_enabled, Time.getFormatTime(snooze - now)));

        long absLastFire = Math.abs(Time.ReadTime(c) - now);

        if (absLastFire > 172800000)
            tv1.setText(getString(R.string.notifsched_toolong));
        else
            tv1.setText(getString(R.string.notifsched_before, Time.getTextTime(absLastFire, c)));
        tv2.setText(Time.getTextTime((Time.ReadTime(c) + 86400000) - now, c));
        tv3.setText(Time.getTextTime(next - now, c));
        tv4.setText(Time.getTextTime(second - now, c));
        tv5.setText(Time.getTextTime(fire225 - now, c));
        tv6.setText(Time.getTextTime(fire235 - now, c));
        tv7.setText(Time.getTextTime(fire245 - now, c));
    }
}