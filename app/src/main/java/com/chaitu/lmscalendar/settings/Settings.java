package com.chaitu.lmscalendar.settings;

import android.content.SharedPreferences;

/**
 * Created by CHAITU on 11/25/2016.
 */

public class Settings {
    public static final String PREF_LASTCALENDARID = "lastCalendarId";
    public static final String PREF_LASTCALENDARNAME = "lastCalendarName";
    public static final String PREF_LASTURL = "lastUrl";
    public static final String PREF_LASTURLPASSWORD = "lastUrlPassword";
    public static final String PREF_LASTURLUSERNAME = "lastUrlUsername";

    public static final String PREF_LASTSAKTZ = "lastSakTZ";
    public static final String PREF_LASTSYNCINTERVAL = "lastSyncInterval";
    private final SharedPreferences mPreferences;

    public Settings(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public int getInt(final String key, final int def) {
        return mPreferences.getInt(key, def);
    }

    public int getInt(final String key) {
        return getInt(key, 0);
    }

    public void putInt(final String key, final int value) {
        mPreferences.edit().putInt(key, value).commit();
    }

    public long getLong(final String key, final long def) {
        return mPreferences.getLong(key, def);
    }

    public long getLong(final String key) {
        return getLong(key, 0);
    }

    public void putLong(final String key, final long value) {
        mPreferences.edit().putLong(key, value).commit();
    }

    public boolean getBoolean(final String key, final boolean def) {
        return mPreferences.getBoolean(key, def);
    }

    public boolean getBoolean(final String key) {
        return getBoolean(key, false);
    }

    public void putBoolean(final String key, final boolean value) {
        mPreferences.edit().putBoolean(key, value).commit();
    }

    public String getString(final String key, final String def) {
        return mPreferences.getString(key, def);
    }

    public String getString(final String key) {
        return getString(key, "");
    }

    public void putString(final String key, final String value) {
        mPreferences.edit().putString(key, value).commit();
    }


}
