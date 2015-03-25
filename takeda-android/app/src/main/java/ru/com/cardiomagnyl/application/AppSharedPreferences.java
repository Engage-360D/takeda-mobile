package ru.com.cardiomagnyl.application;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class AppSharedPreferences {

    public static enum Class {
        bln(Boolean.class),
        flt(Float.class),
        ntg(Integer.class),
        lng(Long.class),
        str(String.class),
        sst(Set.class);

        private final java.lang.Class mPrefClass;

        Class(java.lang.Class prefClass) {
            mPrefClass = prefClass;
        }

        public java.lang.Class getValue() {
            return mPrefClass;
        }
    }

    public static enum Preference {
        tokenId("token", Class.str),
        alarmActions("alarm_actions", Class.sst),
        isr("isr", Class.ntg);

        private final String mPrefName;
        private final Class mClass;

        Preference(String prefName, Class prefClass) {
            mPrefName = prefName;
            mClass = prefClass;
        }

        public String getPrefName() {
            return mPrefName;
        }

        public Class getPrefClass() {
            return mClass;
        }
    }

    public static Object get(Preference preference) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CardiomagnylApplication.getAppContext());

        switch (preference.getPrefClass()) {
            case bln:
                return preferences.getBoolean(preference.getPrefName(), false);
            case flt:
                return preferences.getFloat(preference.getPrefName(), 0.0F);
            case ntg:
                return preferences.getInt(preference.getPrefName(), 0);
            case lng:
                return preferences.getLong(preference.getPrefName(), 0);
            case str:
                return preferences.getString(preference.getPrefName(), "");
            case sst:
                return preferences.getStringSet(preference.getPrefName(), new HashSet<String>());
        }
        return null;
    }

    public static void put(Preference preference, Object value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CardiomagnylApplication.getAppContext());

        switch (preference.getPrefClass()) {
            case bln:
                preferences.edit().putBoolean(preference.getPrefName(), (Boolean) value).commit();
                break;
            case flt:
                preferences.edit().putFloat(preference.getPrefName(), (Float) value).commit();
                break;
            case ntg:
                preferences.edit().putInt(preference.getPrefName(), (Integer) value).commit();
                break;
            case lng:
                preferences.edit().putLong(preference.getPrefName(), (Long) value).commit();
                break;
            case str:
                preferences.edit().putString(preference.getPrefName(), (String) value).commit();
                break;
            case sst:
                preferences.edit().putStringSet(preference.getPrefName(), (Set<String>) value).commit();
                break;
        }
    }

}