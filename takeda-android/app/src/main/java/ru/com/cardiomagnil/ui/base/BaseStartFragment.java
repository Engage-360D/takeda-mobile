package ru.com.cardiomagnil.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class BaseStartFragment extends Fragment {
    public abstract void initParent(Activity activity);
}