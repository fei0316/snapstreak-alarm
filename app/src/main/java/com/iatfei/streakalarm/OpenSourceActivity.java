package com.iatfei.streakalarm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;

import java.lang.reflect.Field;

public class OpenSourceActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_frame);
        //setupActionBar();

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //todo:remove if unnecessary.
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        LibsFragment fragment = new LibsBuilder()
                .withFields(R.string.class.getFields())
                //.withLicenseShown(true)
                //.withVersionShown(true)
                                //.withAboutIconShown(true)
                //.withAboutVersionShown(true)
                //.withAboutDescription("I'm impressed you would actually click into this! You must be super interested in my app! Drop me an email!")
                .fragment();

        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
