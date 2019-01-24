package com.iatfei.streakalarm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class NewAboutActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_frame);

        final Toolbar toolbar = findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new AboutFragment())
                .commit();
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }
    private AppCompatDelegate mDelegate;
    @NonNull
    public AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this,null);
        }
        return mDelegate;
    }
    public static class AboutFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.about);
            Preference ver = findPreference("edit_text_preference_2");
            ver.setSummary(BuildConfig.VERSION_NAME);
            Preference license = findPreference("edit_text_preference_6");
            license.setIntent(new Intent(getActivity(),OpenSourceActivity.class));

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setType("text/html");
            PackageManager packageManager = getActivity().getPackageManager();

            List<ResolveInfo> list = packageManager.queryIntentActivities(emailIntent, 0);

            if(list.size() == 0){
                Preference mail = findPreference("edit_text_preference_8");
                mail.setIntent(null);
            }

            Intent webpageIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));

            List<ResolveInfo> wlist = packageManager.queryIntentActivities(webpageIntent, 0);

            if(wlist.size() == 0) {
                Preference web = findPreference("edit_text_preference_4");
                web.setIntent(null);
            }
        }
    }
}

