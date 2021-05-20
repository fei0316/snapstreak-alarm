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

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ShortcutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent.ShortcutIconResource icon =
                Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_shortcut_sent);

        Intent intent = new Intent();

        Intent launchIntent = new Intent(this, ResetCloseActivity.class);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.shortcut_sent_short));
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        setResult(RESULT_OK, intent);
        finish();
    }


}
