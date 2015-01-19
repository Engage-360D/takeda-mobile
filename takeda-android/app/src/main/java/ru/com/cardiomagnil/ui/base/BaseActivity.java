package ru.com.cardiomagnil.ui.base;

import android.app.Activity;

import ru.com.cardiomagnil.application.CardiomagnilApplication;

public class BaseActivity extends Activity {
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
}
