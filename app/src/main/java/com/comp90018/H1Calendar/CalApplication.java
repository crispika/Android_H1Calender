package com.comp90018.H1Calendar;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;


/**
 * Adjust Application setting for switching theme (Day/Night).
 */
public class CalApplication extends Application {

    final static String LOG_TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        Log.d(LOG_TAG, "UIMode is set to " + (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK));
        super.onCreate();

    }

}
