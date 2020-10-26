package com.iatfei.streakalarm

import android.content.Context
import net.furkanakdemir.noticeboard.util.color.NoticeBoardColorProvider

class ChangelogColorProvider(private val context: Context) : NoticeBoardColorProvider() {
    override var colorAdded: Int = R.color.colorAccent
    override var colorChanged: Int = R.color.colorAccentDark
    override var colorDeprecated: Int = R.color.colorPrimary
    override var colorRemoved: Int = R.color.colorPrimary
    override var colorFixed: Int = R.color.colorPrimaryDark
    override var colorSecurity: Int = R.color.colorPrimaryDark
}