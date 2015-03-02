package ru.com.cardiomagnyl.ui.slidingmenu.content.journal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.ui.base.BaseTimeLineFragment;

public class JournalFragment extends BaseTimeLineFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    protected void onTaskUpdated(Task updatedTask) {
        Activity activity = getActivity();
        ListView listViewTimeline = (ListView) activity.findViewById(R.id.listViewTimeline);
        RadioGroup radioGroupTabs = (RadioGroup) activity.findViewById(R.id.radioGroupTabs);
        TimelineAdapter timelineAdapter = (TimelineAdapter) listViewTimeline.getAdapter();
        timelineAdapter.notifyDataSetChanged();
        udateFullTimelineList(updatedTask);
        separateFullTimeline();
        mCurrentTimelineList.clear();
        mCurrentTimelineList.addAll(radioGroupTabs.getCheckedRadioButtonId() == R.id.radioButtonNew ? mNewTimelineList : mFilledTimelineList);
        timelineAdapter.notifyDataSetInvalidated();
    }

    @Override
    protected void onTimelineUpdated(final View fragmentView) {
        ListView listViewTimeline = (ListView) fragmentView.findViewById(R.id.listViewTimeline);
        RadioGroup radioGroupTabs = (RadioGroup) fragmentView.findViewById(R.id.radioGroupTabs);
        TimelineAdapter timelineAdapter = (TimelineAdapter) listViewTimeline.getAdapter();
        timelineAdapter.notifyDataSetChanged();
        mCurrentTimelineList.clear();
        mCurrentTimelineList.addAll(radioGroupTabs.getCheckedRadioButtonId() == R.id.radioButtonNew ? mNewTimelineList : mFilledTimelineList);
        timelineAdapter.notifyDataSetInvalidated();
    }

    @Override
    protected void initFragmentFinishHelper(final View fragmentView) {
        final RadioGroup radioGroupTabs = (RadioGroup) fragmentView.findViewById(R.id.radioGroupTabs);
        final SwipeRefreshLayout swipeRefreshLayoutTimeline = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipeRefreshLayoutTimeline);
        final ListView listViewTimeline = (ListView) fragmentView.findViewById(R.id.listViewTimeline);

        mCurrentTimelineList.clear();
        mCurrentTimelineList.addAll(mNewTimelineList);
        radioGroupTabs.check(R.id.radioButtonNew);

        swipeRefreshLayoutTimeline.setOnRefreshListener(this);

        final TimelineAdapter timelineAdapter = new TimelineAdapter(JournalFragment.this.getActivity(), mCurrentTimelineList, mPillsMap, this);
        listViewTimeline.setAdapter(timelineAdapter);

        radioGroupTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                timelineAdapter.notifyDataSetChanged();
                mCurrentTimelineList.clear();
                mCurrentTimelineList.addAll(checkedId == R.id.radioButtonNew ? mNewTimelineList : mFilledTimelineList);
                timelineAdapter.notifyDataSetInvalidated();
            }
        });
    }

}
