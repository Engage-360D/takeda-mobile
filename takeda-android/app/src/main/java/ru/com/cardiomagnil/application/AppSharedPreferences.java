package ru.com.cardiomagnil.application;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreferences {

    public static enum PrefClass {
        bln(Boolean.class),
        flt(Float.class),
        ntg(Integer.class),
        lng(Long.class),
        str(String.class);

        private final Class mPrefClass;

        PrefClass(Class prefClass) {
            this.mPrefClass = prefClass;
        }

        public Class getValue() {
            return mPrefClass;
        }
    }

    public static enum Preference {
        patient("token", PrefClass.str);

        private final String mPrefName;
        private final PrefClass mPrefClass;

        Preference(String prefName, PrefClass prefClass) {
            this.mPrefName = prefName;
            this.mPrefClass = prefClass;
        }

        public String getPrefName() {
            return mPrefName;
        }

        public PrefClass getPrefClass() {
            return mPrefClass;
        }
    }

    public static Object get(Preference preference) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CardiomagnilApplication.getAppContext());

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
        }
        return null;
    }

    public static void put(Preference preference, Object value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CardiomagnilApplication.getAppContext());

        switch (preference.getPrefClass()) {
            case bln:
                preferences.edit().putBoolean(preference.getPrefName(), (Boolean)value);
                break;
            case flt:
                preferences.edit().putFloat(preference.getPrefName(), (Float)value);
                break;
            case ntg:
                preferences.edit().putInt(preference.getPrefName(), (Integer)value);
                break;
            case lng:
                preferences.edit().putLong(preference.getPrefName(), (Long)value);
                break;
            case str:
                preferences.edit().putString(preference.getPrefName(), (String)value);
                break;
        }

        preferences.edit().commit();
    }

}