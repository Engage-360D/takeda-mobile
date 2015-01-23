package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;

public class Ca_InformationFragment extends Ca_BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_information, null);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }
}
