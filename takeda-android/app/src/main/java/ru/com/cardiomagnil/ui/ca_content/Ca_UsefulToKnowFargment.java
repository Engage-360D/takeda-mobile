package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;

public class Ca_UsefulToKnowFargment extends Ca_BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_heart, null);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }
}
