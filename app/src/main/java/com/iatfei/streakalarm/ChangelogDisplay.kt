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
                tag("1.4.0")
                showRule(rule)
                source(Source.Dynamic(
                        listOf(
                                Release("26/10/2020", "1.4.0",
                                        listOf(
                                                Release.Change(c.getString(R.string.changelog_140_1), ChangeType.CHANGED),
                                                Release.Change(c.getString(R.string.changelog_140_2), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_140_3), ChangeType.ADDED),
                                                Release.Change(c.getString(R.string.changelog_140_4), ChangeType.FIXED),
                                                Release.Change(c.getString(R.string.changelog_bugfixes), ChangeType.FIXED)
                                        )
                                )
                        )
                ))
                val customColorProvider = ChangelogColorProvider(c)
                colorProvider(customColorProvider)
            }
        }
    }
}