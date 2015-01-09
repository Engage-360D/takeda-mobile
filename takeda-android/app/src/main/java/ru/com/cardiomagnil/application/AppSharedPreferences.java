package ru.com.cardiomagnil.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.SharedPreferences;

public class AppSharedPreferences {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static AppSharedPreferences instance;

    private AppSharedPreferences() {
    }

    public static AppSharedPreferences getInstatce() {
        if (instance == null) {
            synchronized (AppSharedPreferences.class) {
                if (instance == null)
                    instance = new AppSharedPreferences();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    public enum PREFERENCES {
        // token
        token,
        // user
        email, plain_password,
        // results
        results
    };

    private Map<PREFERENCES, String> mPreferences = new HashMap<PREFERENCES, String>();

    public void load() {
        SharedPreferences settings = CardiomagnilApplication.getAppContext().getSharedPreferences(AppConfig.PREFERENCES_NAME, 0);
        for (PREFERENCES preference : PREFERENCES.values()) {
            if (settings.contains(preference.name())) {
                mPreferences.put(preference, settings.getString(preference.name(), ""));
            }
        }
    }

    public void save() {
        SharedPreferences settings = CardiomagnilApplication.getAppContext().getSharedPreferences(AppConfig.PREFERENCES_NAME, 0);
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