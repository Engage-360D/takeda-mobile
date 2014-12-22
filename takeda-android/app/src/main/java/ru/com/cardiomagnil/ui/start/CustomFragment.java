package ru.com.cardiomagnil.ui.start;


import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class CustomFragment extends Fragment {
    boolean mInitParentAtFirstTimeSaved = false;
    boolean mInitParentAtFirstTime = false;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("initParentAtFirstTimeSaved", mInitParentAtFirstTimeSaved);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("initParentAtFirstTimeSaved")) {
            mInitParentAtFirstTimeSaved = savedInstanceState.getBoolean("initParentAtFirstTimeSaved");
            mInitParentAtFirstTime = mInitParentAtFirstTimeSaved;
        }

        if (mInitParentAtFirstTime) {
            mInitParentAtFirstTime = false;
            initParent();
        }
    }

    public abstract void initParent();

    public void setInitParentAtFirstTime(boolean initParentAtFirstTime) {
        mInitParentAtFirstTime = initParentAtFirstTime;
        mInitParentAtFirstTimeSaved = initParentAtFirstTime;
    }
}