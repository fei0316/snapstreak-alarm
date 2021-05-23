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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentString = intent.getAction();
        if (intentString != null) {
            if (intentString.equals(Intent.ACTION_BOOT_COMPLETED) || intentString.equals(Intent.ACTION_MY_PACKAGE_REPLACED)) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                boolean isEnabled = settings.getBoolean("serviceEnabled", false);

                if (isEnabled) {
                    int count = Time.ReadNotifCount(context);
                    if (count > 1)
                        Time.setTally(context, (count - 1));
                    NotificationManage.MakeNotif(context);
                }
            }
        }
    }
}
