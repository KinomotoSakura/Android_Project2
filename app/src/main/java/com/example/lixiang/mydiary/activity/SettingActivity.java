package com.example.lixiang.mydiary.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.lixiang.mydiary.R;
import com.example.lixiang.mydiary.util.ThemeUtils;

public class SettingActivity extends AppCompatActivity {
    private SettingFragment mSettingFragment;
    static public Activity set = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setTitle("设置");
        setContentView(R.layout.activity_setting);

        set = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            mSettingFragment = new SettingFragment();
            replaceFragment(R.id.layout_setting_container,mSettingFragment);
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

    static public class SettingFragment extends PreferenceFragment {
        private Preference mAccount;
        private Preference mTheme;
        private Preference setGesture;
        private View theme_view = null;
        private LayoutInflater factor = null;

        static private Activity activity = null;

        static private Button shadow = null;
        static private Button white = null;
        static private Button pink = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_setting);

            factor = LayoutInflater.from(getActivity());
            theme_view = factor.inflate(R.layout.theme_option, null);
            shadow = (Button)theme_view.findViewById(R.id.shadow);
            white = (Button)theme_view.findViewById(R.id.black);
            pink = (Button)theme_view.findViewById(R.id.pink);

            activity = getActivity();

            mAccount = findPreference("account");
            mTheme = findPreference("theme");
            setGesture = findPreference("gesture");
            mAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(),ModifyPasswordActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            });
            mTheme.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder chglog = new AlertDialog.Builder(getActivity());
                    chglog.setTitle("Themes")
                            .setView(theme_view)
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            }).show();
                    return true;
                }
            });
            setGesture.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(),CreateGestureActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    return true;
                }
            });

            setClick();
        }

        private void setClick(){
            shadow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThemeUtils.setmTheme(1);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            white.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThemeUtils.setmTheme(2);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            pink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ThemeUtils.changeToTheme(getActivity(), 3);
                    ThemeUtils.setmTheme(0);
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }
}
