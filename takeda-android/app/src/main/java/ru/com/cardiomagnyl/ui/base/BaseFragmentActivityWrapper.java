package ru.com.cardiomagnyl.ui.base;

import android.support.v4.app.FragmentActivity;

public interface BaseFragmentActivityWrapper {
    public void showProgressDialog();

    public void hideProgressDialog();

    public FragmentActivity getFragmentActivity();
}
