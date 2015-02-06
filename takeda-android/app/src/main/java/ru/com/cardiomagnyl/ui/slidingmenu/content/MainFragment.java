package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Tools;

public class MainFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slidingmenu_main, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    private void initFragment(final View view) {
        initIndex(view);
        initWeekDateRange(view);
        initToday(view);
        initTablets(view);
    }

    private void initIndex(final View view) {
        int index = 0;
        TestResult testResult = AppState.getInsnatce().getTestResult();
        if (testResult != null) {
            index = testResult.getScore();
        }

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
        View textViewTakeTablets = view.findViewById(R.id.textViewTakeTablets);
        textViewTakeTablets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                    SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                    Fragment fragment = new TakingPillsFragment();
                    slidingMenuActivity.putContentOnTop(fragment, true);
                }
            }
        });
    }
}
