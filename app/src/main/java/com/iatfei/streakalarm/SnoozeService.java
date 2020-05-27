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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class SnoozeService extends IntentService {
    public SnoozeService() {
        super("SnoozeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context c = getApplicationContext();
        NotificationManage.CloseNotif(c);
        NotificationManage.Snooze(c);
        int currentTally = Time.ReadNotifCount(c);
        Time.setTally(c, (currentTally-1));
    }
}
