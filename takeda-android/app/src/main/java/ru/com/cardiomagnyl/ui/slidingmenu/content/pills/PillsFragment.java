package ru.com.cardiomagnyl.ui.slidingmenu.content.pills;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;
import java.util.Set;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.schedule.PillsScheduler;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class PillsFragment extends BaseItemFragment implements SwipeRefreshLayout.OnRefreshListener {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pills, null);
        initFragmentStart(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellAddPill(viewGroupTopBar, true, true);
    }

    @Override
    public void onRefresh() {
        View fragmentView = getView();

        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getPillHttp(fragmentView);

        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.fragmentContent);
        swipeLayout.setRefreshing(false);
    }

    private void initFragmentStart(final View fragmentView) {
        ImageView imageViewPillsInfo = (ImageView) fragmentView.findViewById(R.id.imageViewPillsInfo);
        imageViewPillsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogs.showAlertDialog(fragmentView.getContext(), R.string.pills_info);
            }
        });

        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getPillDatabase(fragmentView);
    }

    private void getPillDatabase(final View fragmentView) {
        final Token token = AppState.getInsnatce().getToken();
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        Set<String> act = (Set<String>) AppSharedPreferences.get(AppSharedPreferences.Preference.alarmActions);
                        if (act.isEmpty() && !pillsList.isEmpty()) PillsScheduler.setAll(pillsList);

                        initFragmentFinish(fragmentView, pillsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        getPillHttp(fragmentView);
                    }
                },
                PillDao.Source.database
        );
    }

    private void getPillHttp(final View fragmentView) {
        final Token token = AppState.getInsnatce().getToken();
        PillDao.getAll(
                token,
                new CallbackOne<List<Pill>>() {
                    @Override
                    public void execute(List<Pill> pillsList) {
                        PillsScheduler.setAll(pillsList);

                        initFragmentFinish(fragmentView, pillsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null);
                    }
                },
                PillDao.Source.http
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Pill> pillsList) {
        final View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        final View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (pillsList == null || pillsList.isEmpty()) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, pillsList);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final List<Pill> pillsList) {
        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.fragmentContent);
        final ListView listViewPills = (ListView) fragmentView.findViewById(R.id.listViewPills);

        swipeLayout.setOnRefreshListener(this);

        final PillsAdapter pillsAdapter = new PillsAdapter(PillsFragment.this.getActivity(), pillsList);
        listViewPills.setAdapter(pillsAdapter);

        listViewPills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPillDetails(pillsList.get(position));
            }
        });
    }

    private void showPillDetails(final Pill pill) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.PILL, pill);

        PillDetailsFragment pillDetailsFragment = new PillDetailsFragment();
        pillDetailsFragment.setArguments(bundle);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.putContentOnTop(pillDetailsFragment, true);
    }
}
