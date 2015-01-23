package ru.com.cardiomagnil.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import ru.com.cardiomagnil.ca_api.db.HelperFactory;

public class CardiomagnilApplication extends Application {
    private static CardiomagnilApplication sCardiomagnilApplication = null;
    private Activity mCurrentActivity = null;
    private static final Object sStaticLockObj = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        sCardiomagnilApplication = this;
        HelperFactory.setHelper(getApplicationContext());
    }

    // FIXME: This method is for use in emulated process environments...
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }

    public static CardiomagnilApplication getInstance() {
        return sCardiomagnilApplication;
    }

    public static Context getAppContext() {
        return (Context) sCardiomagnilApplication;
    }

    public String getTag() {
        return CardiomagnilApplication.this.getClass().getSimpleName();
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

}
