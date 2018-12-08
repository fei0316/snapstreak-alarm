package com.iatfei.streakalarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class resetService extends IntentService {

    public resetService() {
        super("resetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time.ResetTime(getApplicationContext());
        NotificationManager notif =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notif.cancel(2);
    }
}
