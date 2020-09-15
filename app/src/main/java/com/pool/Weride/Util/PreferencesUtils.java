package com.pool.Weride.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    private static final String PREF_NAME = "socialapp";
    private static PreferencesUtils mInstance;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private PreferencesUtils(Context mContext) {
        preferences = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static synchronized PreferencesUtils getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new PreferencesUtils(mContext);
        }
        return mInstance;
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putLong(String key, Long value) {
        editor.putLong(key, value).commit();
    }

    public long getLong(String key) {
        return preferences.getLong(key, 0L);
    }

    public void clearPreferences() {
        if (editor != null) {
            editor.clear().commit();
        }
    }

}
