package com.iatfei.streakalarm;

import android.app.IntentService;
import android.content.Intent;

public class resetService extends IntentService {

    public resetService() {
        super("resetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Time.ResetTime(getApplicationContext());
        NotificationManage.CancelNotif(getApplicationContext());
        NotificationManage.MakeNotif(getApplicationContext());
    }
}
