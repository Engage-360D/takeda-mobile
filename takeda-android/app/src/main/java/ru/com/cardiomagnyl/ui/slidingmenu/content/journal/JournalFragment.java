package ru.com.cardiomagnyl.ui.slidingmenu.content.journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.timeline.Timeline;
import ru.com.cardiomagnyl.model.timeline.TimelineDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.TimelineComparator;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class JournalFragment extends BaseItemFragment {
    private final List<Timeline> mFullTimelineList = new ArrayList<>();
    private final List<Timeline> mNewTimelineList = new ArrayList<>();
    private final List<Timeline> mFilledTimelineList = new ArrayList<>();
    private final List<Timeline> mCurrentTimelineList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    public void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Token token = AppState.getInsnatce().getToken();
        getTimeline(fragmentView, token);
    }

    public void getTimeline(final View fragmentView, final Token token) {
        TimelineDao.getAll(
                token,
                new CallbackOne<List<Timeline>>() {
                    @Override
                    public void execute(List<Timeline> timeline) {
                        Collections.sort(timeline, new TimelineComparator());
                        getPillDatabase(fragmentView, token, timeline);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null, null);
                    }
                }
        );
    }

    public void getPillDatabase(final View fragmentView, final Token token, final List<Timeline> timeline) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        Map<String, Pill> pillsMap = Pill.listToMap(pillsList);
                        if (Timeline.checkPillsInTasks(timeline, pillsMap)) {
                            initFragmentFinish(fragmentView, timeline, pillsMap);
                        } else {
                            getPillHttp(fragmentView, token, timeline);
                        }
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        getPillHttp(fragmentView, token, timeline);
                    }
                },
                PillDao.Source.database
        );
    }

    public void getPillHttp(final View fragmentView, final Token token, final List<Timeline> timeline) {
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        Map<String, Pill> pillsMap = Pill.listToMap(pillsList);
                        if (Timeline.checkPillsInTasks(timeline, pillsMap)) {
                            initFragmentFinish(fragmentView, timeline, pillsMap);
                        } else {
                            initFragmentFinish(fragmentView, timeline, null);
                        }
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, timeline, null);
                    }
                },
                PillDao.Source.http
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (timeline == null || timeline.isEmpty() || pillsMap == null) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
            CustomDialogs.showAlertDialog(slidingMenuActivity, R.string.data_not_found);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, timeline, pillsMap);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final List<Timeline> timeline, final Map<String, Pill> pillsMap) {
        final RadioGroup radioGroupTabs = (RadioGroup) fragmentView.findViewById(R.id.radioGroupTabs);
        final ListView listViewTimeline = (ListView) fragmentView.findViewById(R.id.listViewTimeline);

        mFullTimelineList.clear();
        mFullTimelineList.addAll(timeline);
        separateTimeline();

        mCurrentTimelineList.clear();
        mCurrentTimelineList.addAll(mNewTimelineList);
        radioGroupTabs.check(R.id.radioButtonNew);

        final TimelineAdapter timelineAdapter = new TimelineAdapter(JournalFragment.this.getActivity(), mCurrentTimelineList, pillsMap);
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

    private void separateTimeline() {
        mNewTimelineList.clear();
        mFilledTimelineList.clear();

        for (Timeline timeline : mFullTimelineList) {
            List<Task> newTasks = new ArrayList<>();
            List<Task> filledTasks = new ArrayList<>();

            for (Task task : timeline.getTasks()) {
                if (!task.getIsCompleted()) newTasks.add(task);
                else filledTasks.add(task);
            }

            if (!newTasks.isEmpty()) {
                Timeline newTimeline = new Timeline();
                newTimeline.setDate(timeline.getDate());
                newTimeline.setTasks(newTasks);
                mNewTimelineList.add(newTimeline);
            }

            if (!filledTasks.isEmpty()) {
                Timeline filledTimeline = new Timeline();
                filledTimeline.setDate(timeline.getDate());
                filledTimeline.setTasks(filledTasks);
                mFilledTimelineList.add(filledTimeline);
            }
        }
    }

}
