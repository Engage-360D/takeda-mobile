package ru.com.cardiomagnil.ui.c_base;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.CardiomagnilApplication;

public class C_BaseActivity extends Activity {
    @Override
    protected void onStart() {
        super.onStart();

        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CardiomagnilApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        CardiomagnilApplication.getInstance().clearActivityIfCurrent(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }
}
