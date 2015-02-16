package ru.com.cardiomagnyl.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.MapsInitializer;

import ru.com.cardiomagnyl.api.db.HelperFactory;

public class CardiomagnylApplication extends Application {
    private static CardiomagnylApplication sCardiomagnylApplication = null;
    private Activity mCurrentActivity = null;
    private static final Object sStaticLockObj = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        sCardiomagnylApplication = this;
        HelperFactory.setHelper(getApplicationContext());
    }

    // FIXME: This method is for use in emulated process environments...
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }

    public static CardiomagnylApplication getInstance() {
        return sCardiomagnylApplication;
    }

    public static Context getAppContext() {
        return (Context) sCardiomagnylApplication;
    }

    public String getTag() {
        return CardiomagnylApplication.this.getClass().getSimpleName();
    }

    public Activity getCurrentActivity() {
        synchronized (sStaticLockObj) {
            return mCurrentActivity;
        }
    }

    public void setCurrentActivity(Activity currentActivity) {
        synchronized (sStaticLockObj) {
            mCurrentActivity = currentActivity;
        }
    }

    public void clearActivityIfCurrent(Activity currActivity) {
        synchronized (sStaticLockObj) {
            if (mCurrentActivity != null && mCurrentActivity.equals(currActivity)) {
                mCurrentActivity = null;
            }
        }
    }

    public void logout() {
        getCurrentActivity().finish();
        AppSharedPreferences.put(AppSharedPreferences.Preference.tokenId, null);
        // FIXME: test_results
    }

}
