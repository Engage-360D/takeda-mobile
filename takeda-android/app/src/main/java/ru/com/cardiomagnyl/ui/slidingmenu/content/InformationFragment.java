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
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    private void initFragment(final View fragmentView) {
        final View textViewInfarctionApoplexy = fragmentView.findViewById(R.id.textViewInfarctionApoplexy);
        final View textViewLifestyle = fragmentView.findViewById(R.id.textViewLifestyle);
        final View linearLayoutInfarctionApoplexy = fragmentView.findViewById(R.id.linearLayoutInfarctionApoplexy);
        final View linearLayoutLifestyle = fragmentView.findViewById(R.id.linearLayoutLifestyle);

        textViewInfarctionApoplexy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = linearLayoutInfarctionApoplexy.getVisibility();
                linearLayoutInfarctionApoplexy.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        textViewLifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = linearLayoutLifestyle.getVisibility();
                linearLayoutLifestyle.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }

}
