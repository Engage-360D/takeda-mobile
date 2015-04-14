package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.incidents.Incidents;
import ru.com.cardiomagnyl.model.task.Task;
import ru.com.cardiomagnyl.model.user.Isr;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.base.BaseTimeLineFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.institution.InstitutionsSearchFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.journal.JournalFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.journal.TimelineAdapter;
import ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis.RiskAnalysisResultsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Tools;

public class MainFragment extends BaseTimeLineFragment {
    private TimelineAdapter mTimelineAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initTopBarMenuBellCabinet(mViewGroupTopBar, true, true, true);
        if (!AppState.getInsnatce().getIncidents().isEmpty()) {
            View view = createViewIncidents(inflater);
            return view;
        }

        View view = inflater.inflate(R.layout.fragment_main, null);
        initIndex(view);
        initFragmentStart(view);
        return view;
    }

    private View createViewIncidents(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_main_incident, null);

        TextView textViewIncident = (TextView) view.findViewById(R.id.textViewIncident);
        TextView textViewIncidentDescription = (TextView) view.findViewById(R.id.textViewIncidentDescription);
        Button buttonChoose = (Button) view.findViewById(R.id.buttonChoose);

        Incidents incidents = AppState.getInsnatce().getIncidents();
        String incident = "";
        if (incidents.isHadBypassSurgery()) {
            incident = inflater.getContext().getString(R.string.shunting);
        } else if (incidents.isHadHeartAttackOrStroke()) {
            incident = inflater.getContext().getString(R.string.infarction_or_apoplexy);
        } else {
            incident = inflater.getContext().getString(R.string.incident);
        }

        textViewIncident.setText(incident);
        textViewIncidentDescription.setText(String.format(inflater.getContext().getString(R.string.incident_description), incident));

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                slidingMenuActivity.getSlidingMenu().toggle();
                BaseItemFragment fragment = new InstitutionsSearchFragment();
                slidingMenuActivity.replaceAllContent(fragment, true);
                slidingMenuActivity.selectCurrentItem(fragment);
            }
        });

        return view;

    }

    @Override
    protected void initFragmentFinishHelper(final View fragmentView) {
        initSwipeRefreshLayout(fragmentView);
        initWeekDateRange(fragmentView);
        initToday(fragmentView);
        initTimeLine(fragmentView);
        initTabButtons(fragmentView);
    }

    @Override
    protected void onTaskUpdated(Task updatedTask) {
        udateFullTimelineList(updatedTask);
        initToday(getView());
        initTimeLine(getView());
        initIndex(getView());
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
        String index = "0";
        Isr isr = AppState.getInsnatce().getIsr();
        if (isr != null) index = isr.getId();

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

    private void initTabButtons(final View fragmentView) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        View textViewMyRecommendations = fragmentView.findViewById(R.id.textViewMyRecommendations);
        View textViewMySuccess = fragmentView.findViewById(R.id.textViewMySuccess);
        View linearLayoutConsolidatedReport = fragmentView.findViewById(R.id.linearLayoutConsolidatedReport);

        textViewMyRecommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseItemFragment riskAnalysisResultsFragment = new RiskAnalysisResultsFragment();
                slidingMenuActivity.replaceAllContent(riskAnalysisResultsFragment, true);
                slidingMenuActivity.selectCurrentItem(riskAnalysisResultsFragment);
            }
        });

        textViewMySuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseItemFragment journalFragment = new JournalFragment();
                slidingMenuActivity.replaceAllContent(journalFragment, true);
                slidingMenuActivity.selectCurrentItem(journalFragment);
            }
        });

        linearLayoutConsolidatedReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url.ACCOUNT_REPORTS));
                startActivity(browserIntent);
            }
        });
    }

}
