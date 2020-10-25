package com.iatfei.streakalarm

import android.content.Context
import androidx.fragment.app.FragmentActivity
import net.furkanakdemir.noticeboard.*
import net.furkanakdemir.noticeboard.data.model.Release

class ChangelogDisplay : MainActivity() {
    companion object {
        fun display(act: FragmentActivity, c: Context, ) {
            NoticeBoard(act).pin {
                title(c.getString(R.string.changelog_title))
                displayIn(DisplayOptions.DIALOG)
                tag("1.4.0")
                showRule(ShowRule.Once)
                source(Source.Dynamic(
                        listOf(
                                Release("1 Nov 2020", "1.4.0",
                                        listOf(
                                                Release.Change("Bruh", ChangeType.CHANGED)
                                        )
                                )
                        )
                ))
            }
        }
    }
}