package com.iatfei.streakalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.util.Date;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fab-related
        FloatingActionButton fabcustom = findViewById(R.id.menu_customtime);
        FloatingActionButton fabnow = findViewById(R.id.menu_justnow);
        FloatingActionButton fabsnap = findViewById(R.id.menu_opensnapchat);

        setupClock();
        fabcustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickTime();
            }
        });
        fabnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Time.ResetTime(getApplicationContext());
                setupClock();
            }
        });
        fabsnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Snackbar.make(view, R.string.nosnapapp, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
        stopAlarm.setChecked(serviceEnabled);
        return true;
    }

    public void intentAbout() {
        Intent intent = new Intent(this, NewAboutActivity.class);
        startActivity(intent);
    }

    public void setupClock() {
        String time = Time.ReadFormatTime(getApplicationContext());
        TextView clock = findViewById(R.id.textView2);
        clock.setText(time);
        TextView interval = findViewById(R.id.textView4);
        if (Time.IntInterval(getApplicationContext()) != 0)
            interval.setText(getString(R.string.main_interval, Time.IntInterval(getApplicationContext())));
        else
            interval.setText("Please set a reminder interval using the menu above."); //todo:string
        if (readService()){
            TextView enabled = findViewById(R.id.textView5);
            enabled.setText(getString(R.string.main_service_enable));
        }
        else {
            TextView enabled = findViewById(R.id.textView5);
            enabled.setText(getString(R.string.main_service_disable));
        }
    }

    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("previous_started", false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("previous_started", Boolean.TRUE);
            edit.commit();
            showHelp();
        }
        setupClock();
    }

    public void showHelp() {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.menu)
                .setPrimaryText("Set Time Here")
                .setSecondaryText("Set the time you sent your last streak here. You could also do the same thing on the notification.")
                .show();
    }

    public void PickTime() {
        final SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                getString(R.string.snapbefore_title),
                getString(R.string.snapbefore_ok),
                getString(R.string.snapbefore_cancel)
        );
        long now = System.currentTimeMillis();
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(true);
        dateTimeDialogFragment.setMinimumDateTime(new Date(now - 1000L * 60L * 60L * 25L));
        dateTimeDialogFragment.setMaximumDateTime(new Date(now));
        dateTimeDialogFragment.setDefaultDateTime(new Date(now));
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                long now = System.currentTimeMillis();
                long millis = date.getTime();
                if (millis < now) {
                    Time.SetTime(getApplicationContext(), millis);
                    setupClock();
                } else {
                    invalidTime();
                }
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                setupClock();
            }
        });
        dateTimeDialogFragment.show(getSupportFragmentManager(), "dialog_time");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_stopalarm:
                boolean serviceEnabled;
                serviceEnabled = ! menuItem.isChecked();
                menuItem.setChecked(serviceEnabled);
                setService();
                setupClock();
                return true;
            case R.id.menu_setinterval:
                IntSelMake();
                return true;
            case R.id.about:
                intentAbout();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void invalidTime() {
        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.picker_invalid_time), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void IntSelMake() {
        MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(11)
                .defaultValue(8)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
        View view = findViewById(R.id.menu);
        IntSel(numberPicker, view);
    }

    public void IntSel(final MaterialNumberPicker numberPicker, final View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.interval_title))
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Context c = getApplicationContext();
                        Time.SetInterval(c, numberPicker.getValue());
                        int s = Time.IntInterval(c);
                        setupClock();
                        Snackbar.make(view, getString(R.string.interval_set, s), 5000).show();
                    }
                })
                .show();
    }

    public void TestMakeNotif() {
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 30, Time.LongInterval(getApplicationContext()), pendingIntent);
    }

    public void CancelNotif() {
        Intent intent1 = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.cancel(pendingIntent);
        NotificationManager notif =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notif.cancel(2);
        pendingIntent.cancel();
    }

    public void setService() {
        boolean enabled = readService();
        if (enabled) {
            CancelNotif();
            Snackbar.make(findViewById(R.id.menu), R.string.menu_service_disable, Snackbar.LENGTH_SHORT).show();
        }
        else {
            TestMakeNotif();
            Snackbar.make(findViewById(R.id.menu), R.string.menu_service_enabled, Snackbar.LENGTH_SHORT).show();        }
        }

    public boolean readService() {
        return (PendingIntent.getBroadcast(this, 0,
                new Intent(MainActivity.this, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}