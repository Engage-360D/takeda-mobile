package ru.com.cardiomagnyl.ui.base;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import com.google.analytics.tracking.android.EasyTracker;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.util.Tools;

public abstract class BaseFragmentActivity extends FragmentActivity implements BaseFragmentActivityWrapper {
    private ProgressDialog mProgressDialog;

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

    @Override
    public void showProgressDialog() {
        Tools.hideKeyboard(this);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(getString(R.string.progress_dialog_text));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }

}
