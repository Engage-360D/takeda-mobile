package ru.com.cardiomagnil.ui.c_base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnil.app.R;

public abstract class C_BaseItemFragment extends Fragment {
    private String mItemTitle = "";
    private String mItemDescription = "";
    private int mTopRightIcoId = -1;
    private View.OnClickListener mTopRightIcoOnClickListener = null;

    public String getItemTitle() {
        return mItemTitle;
    }

    public void setItemTitle(String itemTitle) {
        mItemTitle = itemTitle;
    }

    public String getItemDescription() {
        return mItemDescription;
    }

    public void setItemDescription(String description) {
        mItemDescription = description;
    }

    public int getTopRightIcoId() {
        return mTopRightIcoId;
    }

    public void setTopRightIcoId(int topRightIcoId) {
        mTopRightIcoId = topRightIcoId;
    }

    public View.OnClickListener getTopRightIcoOnClickListener() {
        return mTopRightIcoOnClickListener;
    }

    public void setTopRightIcoOnClickListener(View.OnClickListener topRightIcoOnClickListener) {
        mTopRightIcoOnClickListener = topRightIcoOnClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.c_slidingmenu_fragment_dummy, null);
    }
}