package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;

public class InformationFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, null);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }
}
