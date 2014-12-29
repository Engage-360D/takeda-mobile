package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;

public class Ca_TakingPillsFargment extends Ca_BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_taking_pills, null);
        initFargment(view);
        return view;
    }

    @Override
    public String getMenuInetmName() {
        return null;
    }

    @Override
    public View getTopView() {
        return null;
    }

    private void initFargment(View view) {
        View linearLayoutTime1 = view.findViewById(R.id.linearLayoutTime1);
        View linearLayoutTime2 = view.findViewById(R.id.linearLayoutTime1);
        View linearLayoutTime3 = view.findViewById(R.id.linearLayoutTime1);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                    SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
                    Fragment fragment = new Ca_AddPillsFargment();
                    mainActivity.switchContent(fragment);
                }
            }
        };

        linearLayoutTime1.setOnClickListener(onClickListener);
        linearLayoutTime2.setOnClickListener(onClickListener);
        linearLayoutTime3.setOnClickListener(onClickListener);
    }
}
