package com.example.lixiang.mydiary.activity;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.util.ThemeUtils;

public class AboutActivity extends AppCompatActivity {
    private AboutFragment mAboutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("关于");

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
        private Preference star;
        private Preference weibo;
        private Preference github;
        private Preference lockpattern;
        private Preference calendar;
        private Preference author;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);

            mShare = findPreference("share");
            mShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/KinomotoSakura/Android_Project2");
                    intent.setData(content_url);
                    startActivity(intent);
                    return true;
                }
            });
            star = findPreference("star");
            star.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
                    normalDialog.setTitle("应用简介");
                    normalDialog.setMessage("我们期待\n你用文字记录内心的每一次触动\n" +
                            "每一段回忆都有它的价值和意义\n所以 不要刻意去粉饰或抹去它\n" +
                            "年轻人 且行且歌吧\n你的每一个脚印 都在这里");
                    normalDialog.setNegativeButton("Back",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { }
                            });
                    normalDialog.show();
                    return true;
                }
            });
            weibo = findPreference("weibo");
            weibo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://weibo.com/kakery");
                    intent.setData(content_url);
                    startActivity(intent);
                    return true;
                }
            });
            github = findPreference("github");
            github.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/KinomotoSakura");
                    intent.setData(content_url);
                    startActivity(intent);
                    return true;
                }
            });
            lockpattern = findPreference("lockpattern");
            lockpattern.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/sym900728/LockPattern");
                    intent.setData(content_url);
                    startActivity(intent);
                    return true;
                }
            });
            calendar = findPreference("calendar");
            calendar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/ebonet/BonetCalendarView");
                    intent.setData(content_url);
                    startActivity(intent);
                    return true;
                }
            });
            author = findPreference("author");
            author.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
                    normalDialog.setTitle("关于作者");
                    normalDialog.setMessage("本App为中山大学数据科学与计算机学院15级MAD课程期末项目，由小组合作完成。\n组长：李翔。");
                    normalDialog.setNegativeButton("Back",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { }
                            });
                    normalDialog.show();
                    return true;
                }
            });
        }
    }
}
