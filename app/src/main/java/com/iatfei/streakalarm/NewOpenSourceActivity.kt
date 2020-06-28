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

package com.iatfei.streakalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mikepenz.aboutlibraries.LibsBuilder

class NewOpenSourceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_frame)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_bar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val fragment = LibsBuilder()
                .withFields(R.string::class.java.fields) // in some cases it may be needed to provide the R class, if it can not be automatically resolved
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withLicenseShown(true)
                .withAboutAppName(getString(R.string.app_name))
                .withAboutSpecial1(getResources().getString(R.string.about_opensource_logolicense_title))
                .withAboutSpecial1Description("The graphics used in this app are from or adapted from: <br><b>Material Design icons by Google</b>, released under the Apache License Version 2.0. <br><b>Material Design Icons</b> by Austin Andrews (@templarian), released under the MIT License.")
                .withAboutDescription("I'm impressed you would actually click into this! I hope you enjoy my first app.<br>Drop me an email!<br><br>¯\\_(ツ)_/¯<br>讓一切成爲往事。")
                .supportFragment()

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()

    }
}