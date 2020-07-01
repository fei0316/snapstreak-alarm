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

        tv1.setText(Time.readFormatTimeNoSec(c));
        tv2.setText(Time.readLosingTime(c));
        tv3.setText(Time.getFormatTime(next - now));
        tv4.setText(Time.getFormatTime(second - now));
        tv5.setText(Time.getFormatTime(fire225 - now));
        tv6.setText(Time.getFormatTime(fire235 - now));
        tv7.setText(Time.getFormatTime(fire245 - now));
    }
}