package com.cardiomagnil.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPreferences {
    private Context mContext;

    public enum PREFERENCES {
        // token
        access_token, expires_in, token_type, scope, refresh_token
        // user
    };

    private Map<PREFERENCES, String> mPreferences = new HashMap<PREFERENCES, String>();

    public AppSharedPreferences(Context context) {
        mContext = context;
    }

    public void load() {
        SharedPreferences settings = mContext.getSharedPreferences(AppConfig.PREFERENCES_NAME, 0);
        for (PREFERENCES preference : PREFERENCES.values()) {
            if (settings.contains(preference.name())) {
                mPreferences.put(preference, settings.getString(preference.name(), ""));
            }
        }
    }

    public void save() {
        SharedPreferences settings = mContext.getSharedPreferences(AppConfig.PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        for (Entry<PREFERENCES, String> entry : mPreferences.entrySet()) {
            editor.putString(entry.getKey().name(), entry.getValue());
        }
        editor.commit();
    }

    public String getPreference(PREFERENCES preference) {
        return mPreferences.get(preference);
    }

    public void setPreference(PREFERENCES preference, String value) {
        mPreferences.put(preference, value);
    }
}