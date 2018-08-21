package com.iatfei.streakalarm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fab-related
        FloatingActionButton fabcustom = findViewById(R.id.menu_customtime);
        FloatingActionButton fabnow = findViewById(R.id.menu_justnow);
        FloatingActionButton fabsnap = findViewById(R.id.menu_opensnapchat);
        MenuItem setInterval = findViewById(R.id.menu_setinterval);
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

    public void intentAbout() {
        Intent intent = new Intent(this, NewAboutActivity.class);
        startActivity(intent);
    }

    public void setupClock() {
        String time = Time.ReadFormatTime(getApplicationContext());
        TextView clock = findViewById(R.id.textView2);
        clock.setText(time);
    }

    public void onResume() {
        super.onResume();
        setupClock();
    }

    public void PickTime() {
        final SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                getString(R.string.snapbefore_title),
                getString(R.string.snapbefore_ok),
                getString(R.string.snapbefore_cancel)
        );
        dateTimeDialogFragment.startAtTimeView();
        dateTimeDialogFragment.set24HoursMode(true);
        dateTimeDialogFragment.setMinimumDateTime(new Date(System.currentTimeMillis() - 1000L * 60L * 60L * 25L));
        dateTimeDialogFragment.setMaximumDateTime(new Date(System.currentTimeMillis()));
        dateTimeDialogFragment.setDefaultDateTime(new Date(System.currentTimeMillis()));
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                long millis = date.getTime();
                Time.SetTime(getApplicationContext(), millis);
                setupClock();
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
            case R.id.menu_setinterval:
                return true;
            case R.id.menu_stopalarm:
                testNotif();
                return true;
            case R.id.about:
                intentAbout();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void testNotif() {
        long millis_now = System.currentTimeMillis();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        long time_last = pref.getLong("lastsnaptime", 0);
        long time_to_send = (time_last + 86400000) - millis_now;
        long longHours = (time_to_send / 1000 / 60 / 60);
        int showHours = (int) longHours;

        Intent openApp = new Intent(this, MainActivity.class);
        Intent openSnap = getPackageManager().getLaunchIntentForPackage("com.snapchat.android");
        //Intent resetTime = new Intent(this, resetService.class);
        //resetTime.setAction(resetService.ACTION1);
        PendingIntent pendingApp = PendingIntent.getActivity(this, 0, openApp, 0);
        //PendingIntent pendingReset = PendingIntent.getService(this, 154, resetTime, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.ic_close_black_24dp) //for now
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_body, showHours))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingApp);
        //.addAction(R.drawable.ic_stat_name, getString(R.string.just_now), pendingReset);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (openSnap != null) {
            openSnap.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingSnap = PendingIntent.getActivity(this, 0, openSnap, 0);
            nBuilder.addAction(R.drawable.ic_close_black_24dp, getString(R.string.menu_opensnapchat), pendingSnap); //icon temp.
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, nBuilder.build());
    }

   /* //please modularize after test!!!!
    public void testNotif() {
        Intent makeNotif = new Intent(this, MakeNotification.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            this.startForegroundService(makeNotif);
        }
        else {
            this.startService(makeNotif);
        }
    }*/
}
