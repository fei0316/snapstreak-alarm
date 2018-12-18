package com.iatfei.streakalarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class ReadService {
    public static boolean status (Context c) {
        return ((PendingIntent.getBroadcast(c, 0,
                new Intent(c, AlarmReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null) ||
                (PendingIntent.getBroadcast(c, 1,
                        new Intent(c, AlarmReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null) ||
                (PendingIntent.getBroadcast(c, 2,
                        new Intent(c, AlarmReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null) ||
                (PendingIntent.getBroadcast(c, 3,
                        new Intent(c, AlarmReceiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null));
    }
}
