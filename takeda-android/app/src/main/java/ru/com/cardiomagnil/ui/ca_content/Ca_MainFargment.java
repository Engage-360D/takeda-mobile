package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.util.Tools;

public class Ca_MainFargment extends Ca_BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_main, null);
        initFargment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }

    private void initFargment(final View view) {
        initIndex(view);
        initWeekDateRange(view);
        initToday(view);
        initTablets(view);
    }

    private void initIndex(final View view) {
        // FIXME: replace on API ready
        Random random = new Random();
        int index = random.nextInt(80 - 50) + 50;

        TextView textViewIndex = (TextView) view.findViewById(R.id.textViewIndex);
        textViewIndex.setText(String.valueOf(index) + "%");
    }

    private void initWeekDateRange(final View view) {
        Pair<String, String> currentWeekDateRange = Tools.getCurrentWeekDateRange();

        TextView textViewWeekDateRange = (TextView) view.findViewById(R.id.textViewWeekDateRange);
        textViewWeekDateRange.setText(currentWeekDateRange.first + "-" + currentWeekDateRange.second);
    }

    private void initToday(final View view) {
        TextView textViewToday = (TextView) view.findViewById(R.id.textViewToday);
        textViewToday.setText(Tools.getDayOfWeek(0));
    }

    private void initTablets(final View view) {
        View linearLayoutTakeTablets = view.findViewById(R.id.linearLayoutTakeTablets);
        linearLayoutTakeTablets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                    SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                    Fragment fragment = new Ca_TakingPillsFargment();
                    slidingMenuActivity.putContentOnTop(fragment, true);
                }
            }
        });
    }
}
