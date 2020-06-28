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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;

import java.util.Locale;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.main_menu);

        //new fab
        SpeedDialView speedDialView = findViewById(R.id.speedDial1);
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_justnow, R.drawable.ic_done_black_24dp)
                        .setFabBackgroundColor(getResources().getColor(R.color.colorAccent))
                        .setLabel(R.string.menu_justnow)
                        .create());
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_sometimeago, R.drawable.ic_access_time_black_24dp)
                        .setFabBackgroundColor(getResources().getColor(R.color.colorAccent))
                        .setLabel(R.string.menu_customtime)
                        .create());
        speedDialView.addActionItem(
                new SpeedDialActionItem.Builder(R.id.fab_snapchat, R.drawable.camera_outline)
                        .setFabBackgroundColor(getResources().getColor(R.color.snapYellow))
                        .setLabel(R.string.menu_opensnapchat)
                        .create());

        setupClock();

        speedDialView.setOnActionSelectedListener(speedDialActionItem -> {
            switch (speedDialActionItem.getId()) {
                case R.id.fab_justnow:
                    Context c = getApplicationContext();
                    Time.ResetTime(c);
                    NotificationManage.CancelNotif(c);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        enableService();
                        setupClock();
                    }, 200);
                    return false;
                case R.id.fab_sometimeago:
                    PickTime();
                    return false;
                case R.id.fab_snapchat:
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    } else {
                        Snackbar.make(findViewById(R.id.speedDial1), R.string.nosnapapp, Snackbar.LENGTH_SHORT).show();
                    }
                    return false;
                default:
                    return false;
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("previous_started", false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("previous_started", Boolean.TRUE);
            edit.apply();
            showHelp();
            aggressiveWarning();
            chineseWarning();
        }
    }


   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean serviceEnabled = readService();
        MenuItem stopAlarm = menu.findItem(R.id.menu_stopalarm);
        stopAlarm.setEnabled(serviceEnabled);
        return true;
    }

    private void intentAbout() {
        Intent intent = new Intent(this, NewAboutActivity.class);
        startActivity(intent);
    }

    private void setupClock() {
        Context c = getApplicationContext();
        String time = Time.ReadFormatTime(c);
        long longtime = Time.ReadTime(c);
        long now = System.currentTimeMillis();
        TextView clock = findViewById(R.id.textView2);
        clock.setText(time);
        if (( now - longtime ) >= 86400000){
            clock.setTextColor(getResources().getColor(R.color.red_warning));
        }
        else if ((now-longtime) > 72000000)
            clock.setTextColor(getResources().getColor(R.color.orange_warning));
        else
            clock.setTextColor(getResources().getColor(R.color.timeText));

        TextView interval = findViewById(R.id.textView4);
        if (Time.IntInterval(c) != 0)
            interval.setText(convertToEnglishDigits.convert(getResources().getQuantityString(R.plurals.main_interval, Time.IntInterval(c), Time.IntInterval(c), Time.getSnooze(c))));
        else
            interval.setText(getString(R.string.main_setinterval_prompt));

        if (readService()){
            TextView enabled = findViewById(R.id.textView5);
            enabled.setText(getString(R.string.main_service_enable));
        }
        else {
            TextView enabled = findViewById(R.id.textView5);
            enabled.setText(getString(R.string.main_service_disable));
        }
        invalidateOptionsMenu();
    }

    public void onResume() {
        super.onResume();
        setupClock();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean configEnabled = settings.getBoolean("serviceEnabled", false);
        boolean actualEnabled = readService();
        if (configEnabled && !actualEnabled){
            final AlertDialog dialog = BatteryOptimizationUtil.getBatteryOptimizationDialog(this);
            if(dialog != null) {
                dialog.show();
            }
        }
        else if (!configEnabled && actualEnabled){
            SharedPreferences.Editor edit = settings.edit();
            edit.putBoolean("serviceEnabled", Boolean.TRUE);
            edit.apply();
        }

    }

    private void showHelp() {
        final MaterialTapTargetPrompt.Builder menuHelpBuilder = new MaterialTapTargetPrompt.Builder(this)
                .setPrimaryText(this.getString(R.string.tutor_menu_title))
                .setSecondaryText(this.getString(R.string.tutor_menu_content))
                .setIcon(R.drawable.ic_more_vert_black_24dp);
        final Toolbar tb = this.findViewById(R.id.toolbar);
        final View child = tb.getChildAt(2);
        if (child instanceof ActionMenuView)
        {
            final ActionMenuView actionMenuView = ((ActionMenuView) child);
            menuHelpBuilder.setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 1));
        }
        else
        {
            Toast.makeText(this, "No Menu!!", Toast.LENGTH_SHORT)
            .show();
        }

        new MaterialTapTargetSequence()
                .addPrompt(new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.speedDial1)
                    .setPrimaryText(this.getString(R.string.tutor_button_title))
                    .setSecondaryText(this.getString(R.string.tutor_button_content))
                    .create())
                .addPrompt(menuHelpBuilder)
                .show();
    }

    private void PickTime() {
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker(
                this,
                1,
                24,
                2,
                Color.TRANSPARENT, //separator color
                getResources().getColor(R.color.timeText), //textcolor
                35,
                Typeface.NORMAL,
                false,
                false,
                null,
                value -> value + " " + (getResources().getQuantityString(R.plurals.snapbefore_hours, value))
        );

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.snapbefore_title))
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                    Context c = getApplicationContext();
                    int hoursago = numberPicker.getValue();
                    int interval = Time.IntInterval(c);
                    if (hoursago >= interval){
                        Snackbar.make(findViewById(R.id.speedDial1), getString(R.string.picker_invalid_time), 5000).show();
                    }
                    else if (hoursago > 0){
                        long setTime = System.currentTimeMillis() - (hoursago * 1000 * 60 * 60 + 2160000);
                        Time.SetTime(c,setTime);
                        NotificationManage.CancelNotif(c);
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            enableService();
                            setupClock();
                        }, 200);
                    }
                })
                .setNegativeButton(getString(android.R.string.cancel), (dialogInterface, i) -> setupClock())
                .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_stopalarm:
                disableService();
                setupClock();
                return true;
            case R.id.menu_setinterval:
                IntSelMake();
                return true;
            case R.id.menu_snooze:
                SnoozeLengthSet();
                return true;
            case R.id.about:
                intentAbout();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void IntSelMake() {
        final MaterialNumberPicker numberPicker = new MaterialNumberPicker(
                this,
                1,
                22,
                8,
                Color.TRANSPARENT, //separator color
                getResources().getColor(R.color.timeText), //textcolor
                35,
                Typeface.NORMAL,
                false,
                false,
                null,
                value -> value + " " + getResources().getQuantityString(R.plurals.snapbefore_hours, value)
        );
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.interval_title))
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                    Context c = getApplicationContext();
                    Time.SetInterval(c, numberPicker.getValue());
                    int s = Time.IntInterval(c);
                    if (readService()) {
                        NotificationManage.CancelNotif(c);
                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            NotificationManage.MakeNotif(this);
                            setupClock();
                        }, 200);
                        Snackbar.make(findViewById(R.id.speedDial1), convertToEnglishDigits.convert(getResources().getQuantityString(R.plurals.interval_set, s, s)), 5000).show();
                    } else {
                        Snackbar.make(findViewById(R.id.speedDial1), convertToEnglishDigits.convert(getResources().getQuantityString(R.plurals.interval_set_disabled, s, s)), 5000).show();
                    }
                })
                .show();
    }

    private void enableService() {
            NotificationManage.MakeNotif(this);
            Snackbar.make(findViewById(R.id.speedDial1), R.string.menu_service_enabled, Snackbar.LENGTH_SHORT).show();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("serviceEnabled", true);
            editor.apply();
    }

    private void disableService() {
            NotificationManage.CancelNotif(this);
            Snackbar.make(findViewById(R.id.speedDial1), R.string.menu_service_disable, Snackbar.LENGTH_SHORT).show();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("serviceEnabled", false);
            editor.apply();
    }

    private boolean readService() {
        return ReadService.status(this);
    }

    private void aggressiveWarning() {
        String deviceMan = android.os.Build.MANUFACTURER;
        if (deviceMan.equalsIgnoreCase("huawei")) {
            final AlertDialog dialog = BatteryOptimizationUtil.getBatteryOptimizationDialog(this);
            if(dialog != null)
                dialog.show();
        }
    }

    private void chineseWarning() {
        String loca = Locale.getDefault().toString();
        if (loca.contains("zh_TW") || loca.contains("zh_CN")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.chineseHK_title))
                    .setMessage(getString(R.string.chineseHK_content))
                    .setPositiveButton(R.string.snapbefore_ok, (dialog, which) -> {
                        //nothing here...
                    })
                    .show();

        }
    }
    private void SnoozeLengthSet() {
        String[] numbers = new String[360/30];
        for (int i=0; i<numbers.length; i++)
            numbers[i] = Integer.toString(i*30+30);
        NumberPicker np = new NumberPicker(this);
        np.setDisplayedValues(numbers);
        np.setMaxValue(numbers.length-1);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.snooze_length_title))
                .setView(np)
                .setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                    Context c = getApplicationContext();
                    Time.setSnooze(c, np.getValue());
                    int mins = Time.getSnooze(c);
                    Snackbar.make(findViewById(R.id.speedDial1), convertToEnglishDigits.convert(getString(R.string.snooze_set, mins)), Snackbar.LENGTH_LONG).show();
                    setupClock();
                })
                .show();
    }
}