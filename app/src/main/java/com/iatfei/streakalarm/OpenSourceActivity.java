package com.iatfei.streakalarm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;

public class OpenSourceActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_frame);
        //setupActionBar();

        final Toolbar toolbar = findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);

        //todo:remove if unnecessary.
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        LibsFragment fragment = new LibsBuilder()
                .withFields(R.string.class.getFields())
                .withLibraries("FloatingActionButton") //todo:autodetect at github, numberpicker and datetime are from custom xml files. Pull request!!
                .withLicenseShown(true)
                //.withVersionShown(true)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription("I'm impressed you would actually click into this! Hoped you enjoyed my first app.<br>Drop me an email!<br><br>¯\\_(ツ)_/¯<br>讓一切成爲往事。") //todo:string
                .fragment();

        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
