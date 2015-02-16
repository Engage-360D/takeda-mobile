package ru.com.cardiomagnyl.ui.base;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        CardiomagnylApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    protected void onPause() {
        CardiomagnylApplication.getInstance().clearActivityIfCurrent(this);
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
