package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;

public class Ca_MainFargment extends Fragment  {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_main, null);
        initFargment(view);
        return view;
    }

    private void initFargment(View view) {
        View linearLayoutTakeTablets = view.findViewById(R.id.linearLayoutTakeTablets);
        linearLayoutTakeTablets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                    SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
                    Fragment fragment = new Ca_TakingPillsFargment();
                    mainActivity.switchContent(fragment);
                }
            }
        });
    }
}
