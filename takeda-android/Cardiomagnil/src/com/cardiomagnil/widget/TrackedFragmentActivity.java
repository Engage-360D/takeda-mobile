package com.cardiomagnil.widget;

import ru.evilduck.framework.SFBaseFragmentActivity;
import android.app.Activity;
import android.os.Bundle;

import com.cardiomagnil.application.CardiomagnilApplication;

public class TrackedFragmentActivity extends SFBaseFragmentActivity {
    private CardiomagnilApplication mCardiomagnilApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardiomagnilApplication = (CardiomagnilApplication) this.getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardiomagnilApplication.setCurrentActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: EasyTracker
//        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
//            EasyTracker.getInstance(this).activityStart(this);
//        }
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        TODO: EasyTracker
//        if (getResources().getBoolean(R.bool.ga_analytics_enabled)) {
//            EasyTracker.getInstance(this).activityStop(this);
//        }
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = mCardiomagnilApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            mCardiomagnilApplication.setCurrentActivity(null);
        }
    }
}