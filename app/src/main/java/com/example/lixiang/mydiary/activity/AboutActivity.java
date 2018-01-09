package com.example.lixiang.mydiary.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.utils.ThemeUtils;

public class AboutActivity extends AppCompatActivity {
    private AboutFragment mAboutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            mAboutFragment = new AboutFragment();
            replaceFragment(R.id.layout_setting_container,mAboutFragment);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void replaceFragment(int viewId, Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    static public class AboutFragment extends PreferenceFragment {
        private Preference mShare;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);

            mShare = findPreference("share");
            mShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String subject = "My Diary";
                    String content = "My Diary真是好";
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    getActivity().startActivity(Intent.createChooser(shareIntent, "分享到"));
                    return true;
                }
            });

        }
    }
}
