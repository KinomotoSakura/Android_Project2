package com.example.lixiang.mydiary.util;

import android.app.Activity;

import com.example.lixiang.mydiary.R;

public class ThemeUtils{
    private static int sTheme;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity
     * of the same type.
     */
    public static void setmTheme(int theme){
        sTheme = theme;
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case 0:
                activity.setTheme(R.style.AppTheme);
                break;
            case 1:
                activity.setTheme(R.style.Theme_Translucent);
                break;
            case 2:
                activity.setTheme(R.style.AppDark);
                break;
        }
    }
}