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

package com.iatfei.streakalarm

import android.content.Context
import androidx.fragment.app.FragmentActivity
import net.furkanakdemir.noticeboard.*
import net.furkanakdemir.noticeboard.data.model.Release

class ChangelogDisplay : MainActivity() {
    companion object {
        fun display(act: FragmentActivity, c: Context, alwaysShow: Boolean) {
            val rule: ShowRule = if (!alwaysShow)
                ShowRule.Once
            else
                ShowRule.Always

            NoticeBoard(act).pin {
                title(c.getString(R.string.changelog_title))
                displayIn(DisplayOptions.ACTIVITY)
                tag("1.5.0")
                showRule(rule)
                source(Source.Dynamic(
                        listOf(
                                Release("02/07/2021", "1.5.0",
                                        listOf(
                                                Release.Change(c.getString(R.string.changelog_150_2), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_150_3), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_jap_added), ChangeType.ADDED),
//                                                Release.Change(c.getString(R.string.changelog_150_2), ChangeType.ADDED),
//                                                Release.Change(c.getString(R.string.changelog_150_3), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_150_1), ChangeType.CHANGED),
                                                Release.Change(c.getString(R.string.changelog_bugfixes), ChangeType.FIXED)
                                        )
                                ),
                                Release("26/10/2020", "1.4.0",
                                        listOf(
                                                Release.Change(c.getString(R.string.changelog_140_2), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_140_3), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_140_1), ChangeType.CHANGED),
                                                Release.Change(c.getString(R.string.changelog_140_4), ChangeType.FIXED),
                                                Release.Change(c.getString(R.string.changelog_bugfixes), ChangeType.FIXED)
                                        )
                                )
                        )
                ))
                val customColorProvider = ChangelogColorProvider()
                colorProvider(customColorProvider)
            }
        }
    }
}