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

import net.furkanakdemir.noticeboard.ChangeType
import net.furkanakdemir.noticeboard.ChangeType.*
import net.furkanakdemir.noticeboard.DisplayOptions
import net.furkanakdemir.noticeboard.util.color.ColorProvider

class ChangelogColorProvider : ColorProvider {
    override fun getChangeTypeBackgroundColor(changeType: ChangeType): Int {
        return when(changeType) {
            ADDED -> { R.color.changelogAdded }
            REMOVED -> { R.color.changelogRemoved }
            CHANGED -> { R.color.changelogChanged }
            FIXED -> { R.color.changelogFixed }
            SECURITY -> { R.color.changelogFixed }
            DEPRECATED -> { R.color.changelogRemoved }
            UNRELEASED -> { R.color.changelogFixed }
        }
    }

    override fun getBackgroundColor(): Int {
        return R.color.backgroundColor
    }

    override fun getDescriptionColor(): Int {
        return R.color.timeText
    }

    override fun getTitleColor(displayOptions: DisplayOptions): Int {
        return R.color.white
    }

}

//class ChangelogColorProvider : NoticeBoardColorProvider() {
//    override var colorAdded: Int = R.color.colorAccent
//    override var colorChanged: Int = R.color.colorAccentDark
//    override var colorDeprecated: Int = R.color.colorPrimary
//    override var colorRemoved: Int = R.color.colorPrimary
//    override var colorFixed: Int = R.color.colorPrimaryDark
//    override var colorSecurity: Int = R.color.colorPrimaryDark
//}