package com.cardiomagnil.application;

import ru.evilduck.framework.SFApplicationState;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class CardiomagnilApplication extends Application {
    private static Context mAppContext = null;
    private Activity mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = this;
        SFApplicationState.getInstatce().initialize(this);
    }

    public static Context getAppContext() {
        return (Context) mAppContext;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }
}
