package ru.com.cardiomagnil.ui.ca_base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnil.app.R;

public abstract class Ca_BaseItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ca_slidingmenu_fragment_dummy, null);
    }

    public abstract String getMenuItemName();

    public abstract View getTopView();
}