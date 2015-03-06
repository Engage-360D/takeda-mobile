package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.ui.base.BaseTimeLineFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.journal.TimelineAdapter;
import ru.com.cardiomagnyl.util.Tools;

public class MainFragment extends BaseTimeLineFragment {
    private TimelineAdapter mTimelineAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        initIndex(view);
        initFragmentStart(view);
        return view;
    }

    @Override
    protected void initFragmentFinishHelper(final View fragmentView) {
        initSwipeRefreshLayout(fragmentView);
        initWeekDateRange(fragmentView);
        initToday(fragmentView);
        initTimeLine(fragmentView);
    }

    @Override
    protected void onTaskUpdated(Task updatedTask) {
        udateFullTimelineList(updatedTask);
        initToday(getView());
        initTimeLine(getView());
    }

    @Override
    protected void onTimelineUpdated(final View fragmentView) {
        initToday(fragmentView);
        initTimeLine(fragmentView);
    }

    private void initSwipeRefreshLayout(final View fragmentView) {
        SwipeRefreshLayout swipeRefreshLayoutTimeline = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipeRefreshLayoutTimeline);
        swipeRefreshLayoutTimeline.setOnRefreshListener(this);
    }

    private void initIndex(final View view) {
        int index = 0;
        TestResult testResult = AppState.getInsnatce().getTestResult();
        if (testResult != null) index = testResult.getScorePercents();

        TextView textViewIndex = (TextView) view.findViewById(R.id.textViewIndex);
        textViewIndex.setText(String.valueOf(index) + "%");
    }

    private void initWeekDateRange(final View view) {
        Pair<String, String> currentWeekDateRange = Tools.getCurrentWeekDateRange();
        TextView textViewWeekDateRange = (TextView) view.findViewById(R.id.textViewWeekDateRange);
        textViewWeekDateRange.setText(currentWeekDateRange.first + "-" + currentWeekDateRange.second);
    }

    private void initToday(final View fragmentView) {
        TextView textViewToday = (TextView) fragmentView.findViewById(R.id.textViewToday);
        Date date = Tools.dateFromShortDate(mFullTimelineList.get(0).getDate());
        textViewToday.setText(Tools.formatShortHintedDate(date));
    }

    private void initTimeLine(final View fragmentView) {
        if (mTimelineAdapter == null) {
            mTimelineAdapter = new TimelineAdapter(fragmentView.getContext(), null, mPillsMap, this);
        }

        ViewGroup fragmentContent = (ViewGroup) fragmentView.findViewById(R.id.fragmentContent);
        mTimelineAdapter.initTasksHolder(fragmentContent, mFullTimelineList.get(0));
    }

}
